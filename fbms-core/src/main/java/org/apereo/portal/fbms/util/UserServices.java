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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apereo.portal.soffit.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides simple, consistent access to commonly-needed information about users.  <em>This bean
 * would be a good candidate to add to the uPortal-soffit-renderer dependency</em>.
 */
@Service
public class UserServices {

    @Resource(name = "signatureKey")
    private String signatureKey;

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

    /**
     * Custom claims are always multi-valued.
     */
    public List<Object> getCustomClaimValues(String claimName, HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)
                || !authHeader.startsWith(Headers.BEARER_TOKEN_PREFIX)) {
            // No attribute without JWT...
            return Collections.emptyList();
        }

        final String bearerToken = authHeader.substring(Headers.BEARER_TOKEN_PREFIX.length());

        try {
            // Validate & parse the JWT
            final Jws<Claims> claims =
                    Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(bearerToken);
            final List<Object> rslt = claims.getBody().get(claimName, List.class);
            return rslt != null  // The get() method returns null for claims that aren't present;  we need an empty list.
                    ? rslt
                    : Collections.emptyList();
        } catch (Exception e) {
            logger.warn("The specified Bearer token is unusable:  '{}'", bearerToken);
            logger.debug("Failed to validate and/or parse the specified Bearer token", e);
        }

        return Collections.emptyList();

    }


}
