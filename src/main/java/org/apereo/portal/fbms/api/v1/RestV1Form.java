package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Form for the purpose of JSON serialization via Jackson within the v1 REST API.  For
 * the sake of backwards compatibility, the members of this type should never change in their number
 * or nature once the v1 API of FMBS has a full release.
 */
public final class RestV1Form {

    private UUID uuid;
    private int version;
    private JsonNode schema;
    private JsonNode metadata;

    /**
     * Uniquely identifies the {@link RestV1Form}.  Consistent between different versions of the same
     * {@link RestV1Form}.
     */
    public UUID getUuid() {
        return uuid;
    }

    public RestV1Form setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * Starts at <code>1</code> and increments every time a {@link RestV1Form} is updated.
     */
    public int getVersion() {
        return version;
    }

    public RestV1Form setVersion(int version) {
        this.version = version;
        return this;
    }

    /**
     * JSON Schema that defines and constrains the data (JSON) that may be submitted with the
     * {@link RestV1Form}.
     */
    public JsonNode getSchema() {
        return schema;
    }

    public RestV1Form setSchema(JsonNode schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Optional metadata (JSON) associated with the {@link RestV1Form}.  May contain UI rendering hints.
     */
    public JsonNode getMetadata() {
        return metadata;
    }

    public RestV1Form setMetadata(JsonNode metadata) {
        this.metadata = metadata;
        return this;
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
        RestV1Form that = (RestV1Form) o;
        return version == that.version &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, version);
    }

}
