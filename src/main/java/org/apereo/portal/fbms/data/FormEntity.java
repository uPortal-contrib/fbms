package org.apereo.portal.fbms.data;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "FBMS_FORM")
public class FormEntity {

    /**
     * The fname and version of the Form.
     */
    @EmbeddedId
    private VersionedFormIdentifier id;

    @Column(name = "SCHEMA", length=100000, nullable = false)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode schema;

    @Column(name = "METADATA", length=100000)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode metadata;

    public VersionedFormIdentifier getId() {
        return id;
    }

    public void setId(VersionedFormIdentifier id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormEntity that = (FormEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
