/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.portal.fbms.data.SubmissionEntity;
import org.apereo.portal.fbms.data.SubmissionIdentifier;

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
    private Long timestamp;
    private JsonNode answers;

    public static RestV1Submission fromEntity(SubmissionEntity entity) {
        return new RestV1Submission()
                .setUsername(entity.getId().getUsername())
                .setFormFname(entity.getId().getFname())
                .setFormVersion(entity.getId().getVersion())
                .setTimestamp(entity.getId().getTimestamp().getTime())
                .setAnswers(entity.getAnswers());
    }

    public static SubmissionEntity toEntity(RestV1Submission submission) {
        final SubmissionIdentifier id = new SubmissionIdentifier();
        id.setUsername(submission.getUsername());
        id.setFname(submission.getFormFname());
        id.setVersion(submission.getFormVersion());
        id.setTimestamp(new Date(submission.getTimestamp()));
        final SubmissionEntity rslt = new SubmissionEntity();
        rslt.setId(id);
        rslt.setAnswers(submission.getAnswers());
        return rslt;
    }

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
    public Long getTimestamp() {
        return timestamp;
    }

    public RestV1Submission setTimestamp(Long timestamp) {
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
