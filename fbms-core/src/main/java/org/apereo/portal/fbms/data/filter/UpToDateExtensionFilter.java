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

import org.apereo.portal.fbms.data.ExtensionFilterChain;
import org.apereo.portal.fbms.data.ExtensionFilterChainMetadata;
import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormRepository;
import org.apereo.portal.fbms.data.SubmissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * During HTTP GET requests for Submission objects, sets the <code>X-FBMS-UpToDate</code> header
 * which indicates whether the submission was made against the most recent version of the referenced
 * Form object.
 */
@Component
public class UpToDateExtensionFilter extends AbstractExtensionFilter<SubmissionEntity> {

    public static final String UP_TO_DATE_HEADER_NAME = "X-FBMS-UpToDate";

    @Autowired
    private FormRepository formRepository;

    @Override
    public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
        // Applies only to GET requests for SubmissionEntity objects
        return SubmissionEntity.class.equals(metadata.getEntityClass()) &&
                request.getMethod().equalsIgnoreCase("GET");
    }

    @Override
    public SubmissionEntity doFilter(SubmissionEntity entity, HttpServletRequest request,
            HttpServletResponse response, ExtensionFilterChain<SubmissionEntity> chain) {

        final SubmissionEntity rslt = chain.doFilter(entity);

        if (rslt != null) {
            // Set the X-FBMS-UpToDate header
            final FormEntity form = formRepository.findFirstByFnameOrderByVersionDesc(rslt.getId().getFname());
            final boolean upToDate = rslt.getId().getVersion() == form.getId().getVersion();
            response.setHeader(UP_TO_DATE_HEADER_NAME, Boolean.toString(upToDate));
        }

        return rslt;

    }

}
