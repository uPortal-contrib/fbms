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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apereo.portal.fbms.data.ExtensionFilter;
import org.apereo.portal.fbms.data.ExtensionFilterChain;
import org.apereo.portal.fbms.data.ExtensionFilterChainMetadata;
import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormRepository;
import org.apereo.portal.fbms.data.filter.AbstractExtensionFilter;
import org.apereo.portal.fbms.util.JsonServices;
import org.apereo.portal.fbms.util.MessageServices;
import org.apereo.portal.fbms.util.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * If the user matches a set of PAGS group names (or the inverse),
 * remove the specified JSON path(s)
 */
public class JsonRemovalByPagsGroupsExtensionFilter extends AbstractExtensionFilter<FormEntity> {

    private static final Set<HttpMethod> RELEVANT_HTTP_METHODS = new HashSet<HttpMethod>(Arrays.asList(HttpMethod.GET));

    @Autowired
    private UserServices userServices;

    @Autowired
    private JsonServices jsonServices;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private MessageServices messageServices;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> targetGroups;
    private Map<String, List<String>> jsonToRemove;
    private boolean inverseFlag = false;
    private String targetForm = "";

    public JsonRemovalByPagsGroupsExtensionFilter(String targetForm, List<String> targetGroups, Map<String, List<String>> jsonToRemove, boolean inverseFlag) {
        super(ExtensionFilter.ORDER_LATE); // Close to the data source
        this.targetForm = targetForm;
        this.targetGroups = targetGroups;
        this.jsonToRemove = jsonToRemove;
        this.inverseFlag = inverseFlag;
    }

    @Override
    public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
        // Check if this form should use this filter
        // targetForm must be supplied since we need a reference to the json structure
        boolean appliesToForm = !StringUtils.isEmpty(this.targetForm) &&
                FormEntity.class.equals(metadata.getEntityClass()) && // Must be a Form object (not a submission)
                this.targetForm.equalsIgnoreCase(metadata.getFname());
        return appliesToForm && RELEVANT_HTTP_METHODS.contains(HttpMethod.valueOf(request.getMethod().toUpperCase()));
    }

    @Override
    public FormEntity doFilter(FormEntity entity, HttpServletRequest request, HttpServletResponse response, ExtensionFilterChain<FormEntity> chain) {

        FormEntity rslt = entity;
        // Repository interaction
        rslt = chain.doFilter(rslt);
        if ((targetGroups == null) || (jsonToRemove == null) || (rslt == null) || (rslt.getSchema() == null)) {
            //Skip the filter
            logger.warn("Target groups, JSON keys, form, or form schema is null.  Skipping this filter.");
        } else {
            String username = userServices.getUsername(request);
            Set<String> associatedGroups = userServices.getGroups(request);

            final boolean associated = isAssociatedWithATargetGroup(associatedGroups, targetGroups);

            if (associated && !inverseFlag) {
                logger.debug("User [{}] in groups [{}] is associated with at least one of the targeted groups of [{}] " +
                                "and flag is set to remove if user is included.  Removing the JSON at [{}]",
                        username, associatedGroups, targetGroups, jsonToRemove);
                removeJson(jsonToRemove, rslt);
            } else if (!associated && inverseFlag) {
                logger.debug("User [{}] in groups [{}] is not associated with any of the targeted groups of [{}] " +
                                "and flag is set to remove if user is not included.  Removing the JSON at [{}]",
                        username, associatedGroups, targetGroups, jsonToRemove);
                removeJson(jsonToRemove, rslt);
            } else {
                logger.debug("Not changing the JSON - User [{}], groups [{}], targeted groups of [{}], inverse flag [{}], JSON [{}].",
                        username, associatedGroups, targetGroups, inverseFlag, jsonToRemove);
            }
        }

        return rslt;
    }

    private boolean isAssociatedWithATargetGroup(Set<String> associatedGroups, List<String> targetGroups) {
        // targetGroups already checked for null.
        for (String tGroup : targetGroups) {
            if (associatedGroups.contains(tGroup)) {
                return true;
            }
        }
        return false;
    }

    private void removeJson(Map<String, List<String>> jsonPaths, FormEntity form) {
        // We need a reference to the form...
        logger.debug("Before removing [{}], original JSON = [{}]", jsonPaths, form.getSchema().toString());
        for (String pointer : jsonPaths.keySet()) {
            final JsonNode targetedNode = form.getSchema().at(pointer);
            if (!targetedNode.isMissingNode()) {
                try {
                    final ObjectNode objectNode = (ObjectNode) targetedNode;
                    for (String key : jsonPaths.get(pointer)) {
                        logger.debug("Removing [{}]:[{}]", pointer, key);
                        ((ObjectNode) targetedNode).remove(key);
                    }
                } catch (Exception e) {
                    logger.warn("Unable to cast the JSON pointer [{}] to an object and/or remove the key(s) [{}]", pointer, jsonPaths.get(pointer));
                }
            } else {
                logger.warn("Unable to find form [{}] node of [{}]", this.targetForm, pointer);
            }
        }
        logger.debug("After removing [{}], resulting JSON = [{}]", jsonPaths, form.getSchema().toString());

    }
}
