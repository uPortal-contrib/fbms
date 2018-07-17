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
package org.apereo.portal.fbms.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Serves as the primary key for {@link SubmissionEntity} objects.
 */
@Embeddable
public class SubmissionIdentifier implements Serializable {

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "FORM_FNAME", nullable = false)
    private String fname; // TODO:  Regex-based validator

    @Column(name = "FORM_VERSION", nullable = false)
    private int version;

    @Column(name = "TIMESTAMP", nullable = false)
    private Date timestamp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VersionedSubmissionIdentifier{" +
                "username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", version=" + version +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionIdentifier that = (SubmissionIdentifier) o;
        return version == that.version &&
                Objects.equals(username, that.username) &&
                Objects.equals(fname, that.fname) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, fname, version, timestamp);
    }

}
