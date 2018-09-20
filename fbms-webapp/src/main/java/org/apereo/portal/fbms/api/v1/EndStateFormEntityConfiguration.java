package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.portal.fbms.data.FormEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * This bean serves as an empty {@link FormEntity} for use with the Form-Forwarding feature.  Send
 * users to this form -- which has no inputs, but supports messages -- when users complete a
 * complicated workflow.
 */
@Configuration
public class EndStateFormEntityConfiguration extends FormEntity {

    @Value("classpath:/end-state-form-entity.json")
    private Resource endStateFormResource;

    private final ObjectMapper mapper = new ObjectMapper();

    @Bean("endStateFormEntity")
    public FormEntity endStateFormEntity() {
        try (InputStream endStateFormInputStream = endStateFormResource.getInputStream()) {

            final RestV1Form rslt = mapper.readValue(endStateFormInputStream, RestV1Form.class);
            return RestV1Form.toEntity(rslt);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
