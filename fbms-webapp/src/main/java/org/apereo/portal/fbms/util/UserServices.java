package org.apereo.portal.fbms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides simple, consistent access to commonly-needed information about users.  <em>This bean
 * would be a good candidate to add to the uPortal-soffit-renderer dependency</em>.
 */
@Service
public class UserServices {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String getUsername(HttpServletRequest request) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.trace("Processing the following Authentication object:  {}", authentication);

        final String rslt = (String) authentication.getPrincipal();

        logger.debug("Found username '{}' based on the contents of the SecurityContextHolder", rslt);

        // Identification based on Spring Security is required to access Servlet-based APIs
        if (rslt == null) {
            throw new SecurityException("User not identified");
        }

        return rslt;

    }

}
