package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Beans that provide mock data for as long as we don't (yet) have database persistence.
 *
 * @deprecated Remove this class when database persistence is available
 */
@Configuration
@Deprecated
public class MockDataConfiguration {

    @Value("classpath:/static/communication-preferences/json-schema.json")
    private Resource jsonSchema;

    @Value("classpath:/static/communication-preferences/ui-schema.json")
    private Resource uiSchema;

    @Value("classpath:/static/communication-preferences/answers.json")
    private Resource answers;

    private final ObjectMapper mapper = new ObjectMapper();

    @Bean("mockForm")
    public Form mockForm() {
        try (
                InputStream jsonSchemaInputStream = jsonSchema.getInputStream();
                InputStream uiSchemaInputStream = uiSchema.getInputStream()
        ) {

            final JsonNode schema = mapper.readTree(jsonSchemaInputStream);
            final JsonNode ui = mapper.readTree(uiSchemaInputStream);

            return new Form() {
                @Override
                public UUID getUuid() {
                    return UUID.randomUUID();
                }

                @Override
                public int getVersion() {
                    return 1;
                }

                @Override
                public JsonNode getSchema() {
                    return schema;
                }

                @Override
                public JsonNode getMetadata() {
                    return ui;
                }
            };

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean("mockResponse")
    public Response mockResponse() {
        final Form mockForm = mockForm();
        try (
                InputStream answersInputStream = answers.getInputStream()
        ) {

            final Date timestamp = new Date();
            final JsonNode answers = mapper.readTree(answersInputStream);

            return new Response() {
                @Override
                public String getUsername() {
                    return null;
                }

                @Override
                public UUID getFormUuid() {
                    return mockForm.getUuid();
                }

                @Override
                public int getFormVersion() {
                    return mockForm.getVersion();
                }

                @Override
                public Date getTimestamp() {
                    return timestamp;
                }

                @Override
                public JsonNode getAnswers() {
                    return answers;
                }
            };

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
