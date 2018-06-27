package org.apereo.portal.fbms.api.v1;

import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormRepository;
import org.apereo.portal.fbms.util.FnameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST endpoints for accessing and manipulating {@link RestV1Form} objects.
 */
@RestController
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    @Autowired
    private FnameValidator fnameValidator;

    @Autowired
    private FormRepository formRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Provides a collection of forms that are viewable by the present user.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listForms() {
        final List<RestV1Form> rslt = StreamSupport.stream(formRepository.findAll().spliterator(), false)
                .map(entity -> RestV1Form.fromEntity(entity))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rslt);
    }

    /**
     * Obtains the {@link RestV1Form} with the specified <code>fname</code>.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.GET)
    public ResponseEntity getFormByFname(@PathVariable("fname") String fname) {
        if (!fnameValidator.isValid(fname)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The specified fname is invalid");
        }

        final FormEntity entity = formRepository.findMostRecentByFname(fname);
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
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createForm(@RequestBody RestV1Form form) {

        logger.debug("Received the following RestV1Form at {} {}:  {}",
                API_ROOT, RequestMethod.POST, form);

        if (formRepository.existsByFname(form.getFname())) {
            /*
             * We already have a Form with this fname;  we cannot accept this new one.
             */
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A form with the specified fname already exists:  " + form.getFname());
        }

        /*
         * It's a new Form;  set the version number to 1.
         */
        form.setVersion(1);

        formRepository.save(RestV1Form.toEntity(form));

        return new ResponseEntity<>(form, HttpStatus.CREATED);
    }

    /**
     * Updates an existing {@link RestV1Form}, incrementing the version number but retaining the same
     * fname.  NOTE:  the body of the request must specify the correct, new version number.  This
     * requirement prevents multiple submissions of the same JSON from incrementing the version
     * multiple times.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.PUT)
    public ResponseEntity updateForm(@PathVariable("fname") String fname, @RequestBody RestV1Form form) {

        logger.debug("Received the following RestV1Form at {}/{} {}:\n{}",
                API_ROOT, form.getFname(), RequestMethod.PUT, form);

        if (!formRepository.existsByFname(form.getFname())) {
            /*
             * The specified form must already exist
             */
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("A form with the specified fname does not exist:  " + fname);
        } else if (!form.getFname().equals(fname)) {
            /*
             * The specified form must match the fname in the URI
             */
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The fname of the specified form ('"
                            + form.getFname() + "') does not match the fname in the URI ('"
                            + fname + "')");
        }

        final FormEntity entity = formRepository.findMostRecentByFname(fname);
        final int expectedVersion = entity.getId().getVersion() + 1;
        if (!Objects.equals(form.getVersion(), expectedVersion)) {
            /*
             * The specified form must correctly specify the next version number (prevents havoc
             * from repeated requests)
             */
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Bad version number;  expected " + expectedVersion + " was " + form.getVersion());
        }

        /*
         * Save the RestV1Form as a new FormEntity
         */
        formRepository.save(RestV1Form.toEntity(form));

        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    /*
     * Support delete?  What happens to existing responses?
     */

}
