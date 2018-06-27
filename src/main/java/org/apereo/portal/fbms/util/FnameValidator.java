package org.apereo.portal.fbms.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * This simple bean decides whether an fname is valid or not throughout the FBMS system
 */
@Component
public class FnameValidator {

    /**
     * Between 5 and 255 letters, numbers, hyphens, and underscores.
     */
    private static final String FNAME_REGEX = "[a-zA-Z0-9-_]{5,255}";

    private final Pattern pattern = Pattern.compile(FNAME_REGEX);

    public boolean isValid(String fname) {
        return pattern.matcher(fname).matches();
    }

}
