package org.apereo.portal.fbms.api.v1;

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
    public RestV1Form mockForm() {
        try (
                InputStream jsonSchemaInputStream = jsonSchema.getInputStream();
                InputStream uiSchemaInputStream = uiSchema.getInputStream()
        ) {

            return new RestV1Form()
                    .setUuid(UUID.randomUUID())
                    .setVersion(1)
                    .setSchema(mapper.readTree(jsonSchemaInputStream))
                    .setMetadata(mapper.readTree(uiSchemaInputStream));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean("mockResponse")
    public RestV1Response mockResponse() {
        final RestV1Form mockForm = mockForm();
        try (
                InputStream answersInputStream = answers.getInputStream()
        ) {

            return new RestV1Response()
                    .setFormUuid(mockForm.getUuid())
                    .setFormVersion(mockForm.getVersion())
                    .setTimestamp(new Date())
                    .setAnswers(mapper.readTree(answersInputStream));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}