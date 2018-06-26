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
