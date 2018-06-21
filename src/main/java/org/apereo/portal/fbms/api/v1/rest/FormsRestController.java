package org.apereo.portal.fbms.api.v1.rest;

import org.apereo.portal.fbms.api.v1.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * REST endpoints for accessing and manipulating {@link Form} objects.
 */
@RestController
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    // TODO:  Remove!
    @Autowired
    private Form mockForm;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Provides a collection of forms that are viewable by the present user.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Form>> listForms() {
        // TODO: Implement!

        return new ResponseEntity<>(Collections.singletonList(mockForm), HttpStatus.OK);
    }

    /**
     * Obtains the {@link Form} with the specified <code>uuid</code>.
     */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public HttpEntity<Form> getFormById(@PathVariable("uuid") String uuid) {
        // TODO: Implement!

        return new ResponseEntity<>(mockForm, HttpStatus.OK);
    }

    /**
     * Creates a new {@link Form} and assigns it a UUID.
     */
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Form> createForm(@RequestBody RestBodyForm form) {

        logger.debug("Received the following Form at {} {}:  {}", API_ROOT, RequestMethod.POST, form);

        form.setUuid(UUID.randomUUID());

        return new ResponseEntity<>(form, HttpStatus.OK);
    }

}
