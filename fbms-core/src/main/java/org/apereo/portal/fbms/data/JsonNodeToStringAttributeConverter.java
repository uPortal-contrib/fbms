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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class JsonNodeToStringAttributeConverter implements AttributeConverter<JsonNode,String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        String rslt = null;
        if (attribute != null) {
            try {
                rslt = objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to write the specified JSON to a String:  " + attribute);
            }
        }
        return rslt;
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        JsonNode rslt = null;
        if (StringUtils.isNotBlank(dbData)) {
            try {
                rslt = objectMapper.readTree(dbData);
            } catch (IOException e) {
                throw new RuntimeException("Unable to read the specified String into JSON:  " + dbData);
            }
        }
        return rslt;
    }

}
