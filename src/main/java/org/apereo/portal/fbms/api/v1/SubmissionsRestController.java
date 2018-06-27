package org.apereo.portal.fbms.api.v1;

import org.apereo.portal.fbms.util.UserServices;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * REST endpoints for accessing and manipulating {@link RestV1Submission} objects.
 */
@RestController
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(SubmissionsRestController.API_ROOT)
public class SubmissionsRestController {

    public static final String API_ROOT = "/api/v1/submissions";

    @Autowired
    private UserServices userServices;

    // TODO:  Remove!
    @Autowired
    private RestV1Submission mockSubmission;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Obtains the user's most recent {@link RestV1Submission} to the {@link RestV1Form} with the
     * specified fname.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.GET)
    public ResponseEntity getUserSubmission(@PathVariable("fname") String fname, HttpServletRequest request) {
        // TODO: Implement!

        final RestV1Submission rslt = new RestV1Submission()
                .setUsername(userServices.getUsername(request))
                .setFormFname(mockSubmission.getFormFname())
                .setFormVersion(mockSubmission.getFormVersion())
                .setTimestamp(mockSubmission.getTimestamp())
                .setAnswers(mockSubmission.getAnswers());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rslt);
    }

    /**
     * Creates a new {@link RestV1Submission} for this user to the {@link RestV1Form} with the
     * specified fname.  NOTE:  there is no update (HTTP PUT) enpoint available for submissions;  each
     * interaction creates a new {@link RestV1Submission} object.  It's up to clients whether they want
     * to deal with multiple submissions by the same user, or only the most recent.
     */
    @RequestMapping(value = "/{fname}", method = RequestMethod.POST)
    public ResponseEntity respond(@PathVariable("fname") String fname, @RequestBody RestV1Submission submission) {

        logger.debug("Received the following RestV1Submission at {}/{} {}:  {}",
                API_ROOT, fname, RequestMethod.POST, submission);

        // Update the timestamp...
        submission.setTimestamp(new Date());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(submission);
    }

}
