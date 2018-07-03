package org.apereo.portal.fbms.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Serves as the primary key for {@link FormEntity} objects.
 */
@Embeddable
public class FormIdentifier implements Serializable {

    @Column(name = "FORM_FNAME", nullable = false)
    private String fname; // TODO:  Regex-based validator

    @Column(name = "FORM_VERSION", nullable = false)
    private int version;

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

    @Override
    public String toString() {
        return "VersionedIdentifier{" +
                "fname='" + fname + '\'' +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormIdentifier that = (FormIdentifier) o;
        return version == that.version &&
                Objects.equals(fname, that.fname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fname, version);
    }

}
