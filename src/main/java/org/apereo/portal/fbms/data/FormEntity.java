package org.apereo.portal.fbms.data;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FBMS_FORM")
public class FormEntity {

    @EmbeddedId
    private VersionedIdentifier id;

    @Column(name = "SCHEMA", length=100000, nullable = false)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode schema;

    @Column(name = "METADATA", length=100000)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode metadata;

    public VersionedIdentifier getId() {
        return id;
    }

    public void setId(VersionedIdentifier id) {
        this.id = id;
    }

    public JsonNode getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "FormEntity{" +
                "id=" + id +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }

}
