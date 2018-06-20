package org.apereo.portal.fbms.api.v1.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.portal.fbms.api.v1.Form;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link Form} that can be marshaled from the body of an HTTP POST or PUT.
 */
public class RestBodyForm implements Form {

    /*
     * NOTE:  It's not entirely clear at this point what implementations of Form are needed or
     * sensible.  Be prepared for this class to disappear or change significantly.
     */

    private UUID uuid;
    private int version;
    private JsonNode schema;
    private JsonNode metadata;

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public JsonNode getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }

    @Override
    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "RestBodyForm{" +
                "uuid=" + uuid +
                ", version=" + version +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestBodyForm that = (RestBodyForm) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid);
    }

}
