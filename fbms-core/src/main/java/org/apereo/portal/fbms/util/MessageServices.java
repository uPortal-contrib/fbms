package org.apereo.portal.fbms.util;

import org.apereo.portal.fbms.data.ExtensionFilter;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Allows {@link ExtensionFilter} beans to pass messages to the UI in the context of POST, PUT, or
 * DELETE operations.
 */
@Service
public class MessageServices {

    private static final List<String> ALLOWED_HTTP_METHODS = Collections.unmodifiableList(
            Arrays.asList("POST", "PUT", "DELETE")
    );

    private static final String REQUEST_ATTRIBUTE_NAME = MessageServices.class.getName() + ".messages";

    public void addMessage(HttpServletRequest request, String message) {

        if (!ALLOWED_HTTP_METHODS.contains(request.getMethod())) {
            throw new IllegalStateException("FBMS messages are available only for the following " +
                    "HTTP methods:  " + ALLOWED_HTTP_METHODS);
        }

        List<String> messages = (List<String>) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
        if (messages == null) {
            messages = new ArrayList<>();
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, messages);
        }

        messages.add(message);

    }

    public List<String> getMessages(HttpServletRequest request) {
        final List<String> messages = (List<String>) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
        return messages != null
                ? messages
                : Collections.emptyList();
    }

}
