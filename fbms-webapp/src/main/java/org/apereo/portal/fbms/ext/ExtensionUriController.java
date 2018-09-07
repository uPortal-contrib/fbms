package org.apereo.portal.fbms.ext;

import org.apereo.portal.fbms.api.v1.UpdateStatus;
import org.apereo.portal.fbms.data.ExtensionFilterChainAbortException;
import org.apereo.portal.fbms.data.ExtensionFilterChainBuilder;
import org.apereo.portal.fbms.data.ExtensionFilterChainMetadata;
import org.apereo.portal.fbms.util.MessageServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that supports extension URIs.  Requests handled by this controller are (1) available
 * to unauthenticated requests and (2)decorated with an ExtensionFilterChain.
 */
@Controller
@CrossOrigin(origins = "${org.apereo.portal.fbms.api.cors.origins:http://localhost:8080}")
@RequestMapping(ExtensionUriController.API_ROOT)
public class ExtensionUriController {

    /* package-private */ static final String API_ROOT = "/ext";

    @Autowired
    private ExtensionFilterChainBuilder filterChainBuilder;

    @Autowired
    private MessageServices messageServices;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/{fname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity invokeExtensionUri(@PathVariable("fname") String fname, HttpServletRequest request, HttpServletResponse response) {

        try {
            filterChainBuilder.fromSupplier(
                    new ExtensionFilterChainMetadata(fname, ExtensionUriEntity.class),
                    request,
                    response,
                    () -> {
                        logger.debug("Invoking extension URI '{}'", request.getRequestURI());
                        return null;
                    }
            ).get();
        } catch (ExtensionFilterChainAbortException fcae) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UpdateStatus.failure(messageServices.getMessages(request)));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdateStatus.success(messageServices.getMessages(request)));

    }

}
