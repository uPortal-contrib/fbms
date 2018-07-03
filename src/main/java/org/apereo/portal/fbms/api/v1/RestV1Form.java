package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.FormIdentifier;

import java.util.Objects;

/**
 * Represents a Form for the purpose of JSON serialization via Jackson within the v1 REST API.  For
 * the sake of backwards compatibility, the members of this type should never change in their number
 * or nature once the v1 API of FMBS has a full release.
 */
public final class RestV1Form {

    private String fname;
    private int version;
    private JsonNode schema;
    private JsonNode metadata;

    public static RestV1Form fromEntity(FormEntity entity) {
        return new RestV1Form()
                .setFname(entity.getId().getFname())
                .setVersion(entity.getId().getVersion())
                .setSchema(entity.getSchema())
                .setMetadata(entity.getMetadata());
    }

    public static FormEntity toEntity(RestV1Form form) {
        final FormIdentifier id = new FormIdentifier();
        id.setFname(form.getFname());
        id.setVersion(form.getVersion());
        final FormEntity rslt = new FormEntity();
        rslt.setId(id);
        rslt.setSchema(form.getSchema());
        rslt.setMetadata(form.getMetadata());
        return rslt;
    }

    /**
     * Uniquely identifies the {@link RestV1Form}.  Consistent between different versions of the same
     * {@link RestV1Form}.
     */
    public String getFname() {
        return fname;
    }

    public RestV1Form setFname(String fname) {
        this.fname = fname;
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
        return "RestV1Form{" +
                "fname='" + fname + '\'' +
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
                Objects.equals(fname, that.fname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fname, version);
    }

}
