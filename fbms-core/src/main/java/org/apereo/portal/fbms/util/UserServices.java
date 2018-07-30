/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.portal.fbms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<String> getGroups(HttpServletRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Set<String> rslt = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());
        logger.debug("Found the following group affiliations for username='{}':  {}", getUsername(request), rslt);
        return rslt;
    }

}
