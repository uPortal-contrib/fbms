package org.apereo.portal.fbms.api.v1.rest;

import org.apereo.portal.fbms.api.v1.RestV1Form;
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
 * REST endpoints for accessing and manipulating {@link RestV1Form} objects.
 */
@RestController
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    // TODO:  Remove!
    @Autowired
    private RestV1Form mockForm;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Provides a collection of forms that are viewable by the present user.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<RestV1Form>> listForms() {
        // TODO: Implement!

        return new ResponseEntity<>(Collections.singletonList(mockForm), HttpStatus.OK);
    }

    /**
     * Obtains the {@link RestV1Form} with the specified <code>uuid</code>.
     */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public HttpEntity<RestV1Form> getFormById(@PathVariable("uuid") String uuid) {
        // TODO: Implement!

        return new ResponseEntity<>(mockForm, HttpStatus.OK);
    }

    /*
     * NOTE:  when we get to it, use URIs of the following form for obtaining previous versions of a
     * form...
     *
     *   - /api/v1/forms/{uuid}/versions/{versionNumber}
     */

    /**
     * Creates a new {@link RestV1Form} and assigns it a UUID.
     */
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<RestV1Form> createForm(@RequestBody RestV1Form form) {

        logger.debug("Received the following RestV1Form at {} {}:  {}", API_ROOT, RequestMethod.POST, form);

        form.setUuid(UUID.randomUUID());

        return new ResponseEntity<>(form, HttpStatus.CREATED);
    }

    /**
     * Updates an existing {@link RestV1Form}, incrementing the version number but retaining the same
     * UUID.  NOTE:  the body of the request must specify the correct, new version number.  This
     * requirement prevents multiple submissions of the same JSON from incrementing the version
     * multiple times.
     */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public HttpEntity<RestV1Form> updateForm(@PathVariable("uuid") String uuid, @RequestBody RestV1Form form) {

        logger.debug("Received the following RestV1Form at {}/{uuid} {}:  {}", API_ROOT, RequestMethod.PUT, form);

        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    /*
     * Support delete?  What happens to existing responses?
     */

}
