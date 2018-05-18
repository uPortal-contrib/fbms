package org.apereo.portal.fbms.api.v1.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.portal.fbms.api.v1.Form;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * REST endpoints for accessing and manipulating {@link Form} objects.
 */
@RestController
@RequestMapping(FormsRestController.API_ROOT)
public class FormsRestController {

    public static final String API_ROOT = "/api/v1/forms";

    // TODO:  Remove!
    @Value("classpath:/static/communication-preferences/json-schema.json")
    private Resource jsonSchema;

    // TODO:  Remove!
    private Form mockForm;

    // TODO:  Remove!
    @PostConstruct
    public void mockData() {

        final ObjectMapper mapper = new ObjectMapper();
        try (InputStream inpt = jsonSchema.getInputStream()) {

            final JsonNode schema = mapper.readTree(inpt);
            mockForm = new Form() {
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
            };

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Provides a collection of forms that are viewable by the present user.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Form>> listForms() {
        // TODO: Implement!

        return new ResponseEntity<>(Collections.singletonList(mockForm), HttpStatus.OK);
    }

    /**
     * Obtains the {@link Form} with the spemcified <code>formId</code>.
     */
    @RequestMapping(value = API_ROOT + "/{uuid}", method = RequestMethod.GET)
    public HttpEntity<Form> getFormById(@PathVariable("uuid") String uuid) {
        // TODO: Implement!

        return new ResponseEntity<>(mockForm, HttpStatus.OK);
    }

}
