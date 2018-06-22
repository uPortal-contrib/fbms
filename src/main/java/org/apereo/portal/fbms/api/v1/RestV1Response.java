package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Response for the purpose of JSON serialization via Jackson within the v1 REST API.
 * For the sake of backwards compatibility, the members of this type should never change in their
 * number or nature once the v1 API of FMBS has a full release.
 */
public final class RestV1Response {

    private String username;
    private UUID formUuid;
    private int formVersion;
    private Date timestamp;
    private JsonNode answers;

    /**
     * The username of the user who produced this RestV1Response.
     */
    public String getUsername() {
        return username;
    }

    public RestV1Response setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * The UUID of the {@link RestV1Form} to which this RestV1Response applies.
     */
    public UUID getFormUuid() {
        return formUuid;
    }

    public RestV1Response setFormUuid(UUID formUuid) {
        this.formUuid = formUuid;
        return this;
    }

    /**
     * The version number of the {@link RestV1Form} at the time this RestV1Response was made.
     */
    public int getFormVersion() {
        return formVersion;
    }

    public RestV1Response setFormVersion(int formVersion) {
        this.formVersion = formVersion;
        return this;
    }

    /**
     * The moment at which this response was created.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    public RestV1Response setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * JSON representing the user's answers to the questions contained in the {@link RestV1Form}.
     */
    public JsonNode getAnswers() {
        return answers;
    }

    public RestV1Response setAnswers(JsonNode answers) {
        this.answers = answers;
        return this;
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
        RestV1Response that = (RestV1Response) o;
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
