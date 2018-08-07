/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.portal.fbms.api.v1;

import org.apereo.portal.fbms.data.ExtensionFilterChainBuilder;
import org.apereo.portal.fbms.data.ExtensionFilterChainMetadata;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormRepository;
import org.apereo.portal.fbms.util.FnameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST endpoints for accessing and manipulating {@link RestV1Form} objects.
 */
@Controller
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    @Autowired
    private FnameValidator fnameValidator;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ExtensionFilterChainBuilder filterChainBuilder;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Provides a collection of forms that are viewable by the present user.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listForms() {
        final List<RestV1Form> rslt = StreamSupport.stream(formRepository.findAll().spliterator(), false)
                .map(RestV1Form::fromEntity)
                .collect(Collectors.toList());

        // TODO:  Should return the URIs of the objects, not the objects themselves.

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rslt);
    }

    /**
     * Obtains the {@link RestV1Form} with the specified <code>fname</code>.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFormByFname(@PathVariable("fname") String fname,
            HttpServletRequest request, HttpServletResponse response) {

        if (!fnameValidator.isValid(fname)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The specified fname is invalid:  " + fname);
        }

        final FormEntity entity =
                filterChainBuilder.fromSupplier(new ExtensionFilterChainMetadata(fname, FormEntity.class),
                        request, response, () -> formRepository.findMostRecentByFname(fname)).get();

        if (entity != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(RestV1Form.fromEntity(entity));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("A form with the specified fname does not exist:  " + fname);
        }

    }

    /*
     * NOTE:  when we get to it, use URIs of the following form for obtaining previous versions of a
     * form...
     *
     *   - /api/v1/forms/{fname}/versions/{versionNumber}
     */

    /**
     * Creates a new {@link RestV1Form} and assigns it an fname.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateStatus> createForm(@RequestBody RestV1Form form, HttpServletRequest request,
            HttpServletResponse response) {

        logger.debug("Received the following RestV1Form at {} {}:  {}",
                API_ROOT, RequestMethod.POST, form);

        if (formRepository.existsByFname(form.getFname())) {
            /*
             * We already have a Form with this fname;  we cannot accept this new one.
             */
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(UpdateStatus.failure("A form with the specified fname already exists:  " + form.getFname()));
        } else if (!fnameValidator.isValid(form.getFname())) {
            /*
             * The submitted Form contains an invalid fname
             */
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(UpdateStatus.failure("The specified fname is invalid:  " + form.getFname()));
        }

        /*
         * It's a new Form;  set the version number to 1.
         */
        form.setVersion(1);

        final FormEntity entity = filterChainBuilder.fromUnaryOperator(
                new ExtensionFilterChainMetadata(form.getFname(), FormEntity.class),
                RestV1Form.toEntity(form),
                request,
                response,
                (e) -> formRepository.save(e)
        ).get();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UpdateStatus.success("Created form with the following fname:  " + form.getFname()));

    }

    /**
     * Updates an existing {@link RestV1Form}, incrementing the version number but retaining the same
     * fname.  NOTE:  the body of the request must specify the correct, new version number.  This
     * requirement prevents multiple submissions of the same JSON from incrementing the version
     * multiple times.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateStatus> updateForm(@PathVariable("fname") String fname,
            @RequestBody RestV1Form form, HttpServletRequest request, HttpServletResponse response) {

        logger.debug("Received the following RestV1Form at {}/{} {}:\n{}",
                API_ROOT, form.getFname(), RequestMethod.PUT, form);

        if (!formRepository.existsByFname(form.getFname())) {
            /*
             * The specified form must already exist
             */
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(UpdateStatus.failure("A form with the specified fname does not exist:  " + fname));
        } else if (!form.getFname().equals(fname)) {
            /*
             * The specified form must match the fname in the URI
             */
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UpdateStatus.failure("The fname of the specified form ('"
                            + form.getFname() + "') does not match the fname in the URI ('"
                            + fname + "')"));
        }

        final FormEntity previousVersion = formRepository.findMostRecentByFname(fname);
        final int expectedVersionNumber = previousVersion.getId().getVersion() + 1;
        if (!Objects.equals(form.getVersion(), expectedVersionNumber)) {
            /*
             * The specified form must correctly specify the next version number (prevents havoc
             * from repeated requests)
             */
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UpdateStatus.failure("Bad version number;  expected " +
                            expectedVersionNumber + " was " + form.getVersion()));
        }

        /*
         * Save the RestV1Form as a new FormEntity
         */
        final FormEntity entity = filterChainBuilder.fromUnaryOperator(
                new ExtensionFilterChainMetadata(fname, FormEntity.class),
                RestV1Form.toEntity(form),
                request,
                response,
                (e) -> formRepository.save(e)
        ).get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdateStatus.success("Updated form with the following fname:  " + fname));

    }

    /*
     * Support delete?  What happens to existing submissions?
     */

}
