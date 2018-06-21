package org.apereo.portal.fbms.api.v1.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.portal.fbms.UserServices;
import org.apereo.portal.fbms.api.v1.Form;
import org.apereo.portal.fbms.api.v1.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * REST endpoints for accessing and manipulating {@link Response} objects.
 */
@RestController
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(ResponsesRestController.API_ROOT)
public class ResponsesRestController {

    public static final String API_ROOT = "/api/v1/responses";

    @Autowired
    private UserServices userServices;

    // TODO:  Remove!
    @Autowired
    private Response mockResponse;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Obtains the user's {@link Response} to the {@link Form} with the specified <code>uuid</code>.
     */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public HttpEntity<Response> getResponseToForm(@PathVariable("uuid") String uuid, HttpServletRequest request) {
        // TODO: Implement!

        final Response rslt = new Response() {
            @Override
            public String getUsername() {
                return userServices.getUsername(request);
            }

            @Override
            public UUID getFormUuid() {
                return mockResponse.getFormUuid();
            }

            @Override
            public int getFormVersion() {
                return mockResponse.getFormVersion();
            }

            @Override
            public Date getTimestamp() {
                return mockResponse.getTimestamp();
            }

            @Override
            public JsonNode getAnswers() {
                return mockResponse.getAnswers();
            }
        };
        return new ResponseEntity<>(rslt, HttpStatus.OK);
    }


}
