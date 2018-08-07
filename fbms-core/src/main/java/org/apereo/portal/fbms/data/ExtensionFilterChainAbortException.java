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

/**
 * Implementations of {@link ExtensionFilter} may throw exceptions of this type in their
 * <code>doFilter</code> method to indicate that the opperation must not proceed, but <strong>only
 * before <code>chain.doFilter()</code> is called</strong>.  Most commonly these exceptions signal
 * field validation issues.
 *
 * <p>In addition to the standard <code>message</code> property, exceptions of this type may
 * optionally include a <code>feedback</code> property that will be communicated to the front-end
 * for possible display to users.
 */
public class ExtensionFilterChainAbortException extends RuntimeException {

    private final String feedback;

    public ExtensionFilterChainAbortException() {
        this.feedback = null;
    }

    public ExtensionFilterChainAbortException(String message) {
        super(message);
        this.feedback = null;
    }

    public ExtensionFilterChainAbortException(String message, Throwable cause) {
        super(message, cause);
        this.feedback = null;
    }

    public ExtensionFilterChainAbortException(Throwable cause) {
        super(cause);
        this.feedback = null;
    }

    public ExtensionFilterChainAbortException(String message, String feedback) {
        super(message);
        this.feedback = feedback;
    }

    public ExtensionFilterChainAbortException(String message, Throwable cause, String feedback) {
        super(message, cause);
        this.feedback = feedback;
    }

    public ExtensionFilterChainAbortException(Throwable cause, String feedback) {
        super(cause);
        this.feedback = feedback;
    }

    /**
     * Message primarily intended for user viewing.
     */
    public String getFeedback() {
        return feedback;
    }

}
