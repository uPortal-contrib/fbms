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
package org.apereo.portal.fbms.data;

import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provide an implementation of this interface to inject custom logic into FBMS when data is
 * created, read, updated, or deleted.  Filter objects always surround repository interactions.
 * You can execute custom logic either before or after the repository does it's part.
 *
 * <p>Filters may throw exceptions <i>before</i> repository operations to cancel them.
 */
public interface ExtensionFilter<E extends FbmsEntity> extends Ordered {

    /*
     * Helper constants for ordering.  For "inbound" processing, <em>earlier</em> filters have their
     * <code>doFilter</code> method invoked before <em>later</em> filters;  for "outbound"
     * processing,it's the reverse.  (In other words "first in, last out.")
     */
    int ORDER_EARLIEST = 1000;
    int ORDER_VERY_EARLY = 100;
    int ORDER_EARLY = 10;
    int ORDER_NORMAL = 0;
    int ORDER_LATE = -10;
    int ORDER_VERY_LATE = -100;
    int ORDER_LATEST = -1000;

    /**
     * This method allows {@link ExtensionFilter} to indicate whether they apply to the specified
     * request and/or entity.  The decision can be based on HTTP method, URI, the user who sent it,
     * etc.  Filters that do not apply are not included in the filter chain.
     */
    boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request);

    /**
     * Invoke custom logic when an entity is created, read, updated, or deleted.
     */
    E doFilter(E entity, HttpServletRequest request, HttpServletResponse response, ExtensionFilterChain<E> chain);

}
