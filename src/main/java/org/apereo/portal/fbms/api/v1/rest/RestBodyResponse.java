package org.apereo.portal.fbms.api.v1.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.portal.fbms.api.v1.Response;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link Response} that can be marshaled from the body of an HTTP POST or PUT.
 */
public class RestBodyResponse implements Response {

    /*
     * NOTE:  It's not entirely clear at this point what implementations of Response are needed or
     * sensible.  Be prepared for this class to disappear or change significantly.
     */

    private String username;
    private UUID formUuid;
    private int formVersion;
    private Date timestamp;
    private JsonNode answers;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public UUID getFormUuid() {
        return formUuid;
    }

    public void setFormUuid(UUID formUuid) {
        this.formUuid = formUuid;
    }

    @Override
    public int getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(int formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public JsonNode getAnswers() {
        return answers;
    }

    public void setAnswers(JsonNode answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "RestBodyResponse{" +
                "username='" + username + '\'' +
                ", formUuid=" + formUuid +
                ", formVersion=" + formVersion +
                ", timestamp=" + timestamp +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestBodyResponse that = (RestBodyResponse) o;
        return formVersion == that.formVersion &&
                Objects.equals(username, that.username) &&
                Objects.equals(formUuid, that.formUuid) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, formUuid, formVersion, timestamp);
    }

}
