package org.apereo.portal.fbms.api.v1;

import org.apereo.portal.fbms.UserServices;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * REST endpoints for accessing and manipulating {@link RestV1Response} objects.
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
    private RestV1Response mockResponse;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Obtains the user's most recent {@link RestV1Response} to the {@link RestV1Form} with the
     * specified fname.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.GET)
    public HttpEntity<RestV1Response> getResponseToForm(@PathVariable("fname") String fname, HttpServletRequest request) {
        // TODO: Implement!

        final RestV1Response rslt = new RestV1Response()
                .setUsername(userServices.getUsername(request))
                .setFormFname(mockResponse.getFormFname())
                .setFormVersion(mockResponse.getFormVersion())
                .setTimestamp(mockResponse.getTimestamp())
                .setAnswers(mockResponse.getAnswers());

        return new ResponseEntity<>(rslt, HttpStatus.OK);
    }

    /**
     * Creates a new {@link RestV1Response} for this user to the {@link RestV1Form} with the
     * specified fname.  NOTE:  there is no update (HTTP PUT) enpoint available for responses;  each
     * submission creates a new {@link RestV1Response} object.  It's up to clients whether they want
     * to deal with multiple responses by the same user, or only the most recent.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.POST)
    public HttpEntity<RestV1Response> respond(@PathVariable("fname") String fname, @RequestBody RestV1Response response) {

        logger.debug("Received the following RestV1Response at {}/fname {}:  {}", API_ROOT, RequestMethod.POST, response);

        // Update the timestamp...
        response.setTimestamp(new Date());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
