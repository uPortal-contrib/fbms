package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a Submission for the purpose of JSON serialization via Jackson within the v1 REST API.
 * For the sake of backwards compatibility, the members of this type should never change in their
 * number or nature once the v1 API of FMBS has a full release.
 */
public final class RestV1Submission {

    private String username;
    private String formFname;
    private int formVersion;
    private Date timestamp;
    private JsonNode answers;

    /**
     * The username of the user who produced this Submission.
     */
    public String getUsername() {
        return username;
    }

    public RestV1Submission setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * The fname of the {@link RestV1Form} to which this Submission applies.
     */
    public String getFormFname() {
        return formFname;
    }

    public RestV1Submission setFormFname(String formFname) {
        this.formFname = formFname;
        return this;
    }

    /**
     * The version number of the {@link RestV1Form} at the time this Submission was made.
     */
    public int getFormVersion() {
        return formVersion;
    }

    public RestV1Submission setFormVersion(int formVersion) {
        this.formVersion = formVersion;
        return this;
    }

    /**
     * The moment at which this Submission was created.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    public RestV1Submission setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * JSON representing the user's answers to the questions contained in the {@link RestV1Form}.
     */
    public JsonNode getAnswers() {
        return answers;
    }

    public RestV1Submission setAnswers(JsonNode answers) {
        this.answers = answers;
        return this;
    }

    @Override
    public String toString() {
        return "RestV1Submission{" +
                "username='" + username + '\'' +
                ", formFname='" + formFname + '\'' +
                ", formVersion=" + formVersion +
                ", timestamp=" + timestamp +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestV1Submission that = (RestV1Submission) o;
        return formVersion == that.formVersion &&
                Objects.equals(username, that.username) &&
                Objects.equals(formFname, that.formFname) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, formFname, formVersion, timestamp);
    }

}
