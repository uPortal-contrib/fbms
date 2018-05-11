package org.jasig.portal.fbms.rest;

import org.jasig.portal.fbms.Form;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Form>> listForms() {
        // TODO: Implement!

        final Form form = new Form() {
            @Override
            public UUID getUuid() {
                return UUID.randomUUID();
            }

            @Override
            public int getVersion() {
                return 1;
            }
        };

        return new ResponseEntity<>(Collections.singletonList(form), HttpStatus.OK);
    }


}
