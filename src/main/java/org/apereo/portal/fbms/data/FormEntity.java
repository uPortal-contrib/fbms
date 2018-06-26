package org.apereo.portal.fbms.data;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FBMS_FORM")
public class FormEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "FNAME", nullable = false, unique = true)
    private String fname; // TODO:  Regex-based validator

    @Column(name = "VERSION", nullable = false)
    private int version;

    @Column(name = "SCHEMA", length=100000, nullable = false)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode schema;

    @Column(name = "METADATA", length=100000)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode metadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
                ", fname='" + fname + '\'' +
                ", version=" + version +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }

}
