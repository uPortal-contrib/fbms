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
package org.apereo.portal.fbms.data.filter;

import org.apereo.portal.fbms.data.ExtensionFilter;
import org.apereo.portal.fbms.data.ExtensionFilterChain;
import org.apereo.portal.fbms.data.ExtensionFilterChainMetadata;
import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.SubmissionEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides support for the FBMS <em>Form Forwarding</em> feature, through which FBMS can send the
 * user to a second (or subsequent) form when he or she completes a form.
 */
@Component
public class FormForwardingExtensionFilter extends AbstractExtensionFilter<SubmissionEntity> {

    public static final String FORM_FORWARD_HEADER_NAME = "X-FBMS-FormForward";

    private static final String FORM_FORWARD_REQUEST_ATTRIBUTE = FormForwardingExtensionFilter.class.getName() + ".formForward";

    public FormForwardingExtensionFilter() {
        super(ExtensionFilter.ORDER_EARLIEST); // First-in, last-out
    }

    @Override
    public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
        // Applies only when a Submission is posted
        return SubmissionEntity.class.equals(metadata.getEntityClass())
                && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * Use this method (within another, custom {@link ExtensionFilter}) to invoke the form-forwarding feature.
     */
    public void forward(HttpServletRequest request, FormEntity form) {

        if (form == null) {
            throw new IllegalArgumentException("Argument 'form' cannot be null");
        }

        request.setAttribute(FORM_FORWARD_REQUEST_ATTRIBUTE, form);

    }

    /**
     * Answers the question whether the specified HTTP request already contains form-forwarding
     * instructions or not.  There may be use cases where users must be forwarded to form B if they
     * haven't already been directed to form A.
     */
    public boolean hasForward(HttpServletRequest request) {
        return request.getAttribute(FORM_FORWARD_REQUEST_ATTRIBUTE) != null;
    }

    @Override
    public SubmissionEntity doFilter(SubmissionEntity entity, HttpServletRequest request, HttpServletResponse response, ExtensionFilterChain<SubmissionEntity> chain) {

        final SubmissionEntity rslt = chain.doFilter(entity);

        final FormEntity form = (FormEntity) request.getAttribute(FORM_FORWARD_REQUEST_ATTRIBUTE);
        if (form != null) {
            // Send the user on to the specified form...
            response.setHeader(FORM_FORWARD_HEADER_NAME, form.getId().getFname());
        }

        return rslt;

    }

}
