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
@Table(name = "FBMS_SUBMISSION")
public class SubmissionEntity implements FbmsEntity {

    /**
     * The fname and version of the associated Form, plus the user's username.
     */
    @EmbeddedId
    private SubmissionIdentifier id;

    @Column(name = "ANSWERS", length=100000, nullable = false)
    @Convert(converter = JsonNodeToStringAttributeConverter.class)
    @Lob
    private JsonNode answers;

    public SubmissionIdentifier getId() {
        return id;
    }

    public void setId(SubmissionIdentifier id) {
        this.id = id;
    }

    public JsonNode getAnswers() {
        return answers;
    }

    public void setAnswers(JsonNode answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "SubmissionEntity{" +
                "id=" + id +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionEntity that = (SubmissionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
