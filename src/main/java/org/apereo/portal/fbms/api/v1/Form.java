package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

/**
 * Forms in FBMS are tree structures that support nested elements to arbitrary depth.  This
 * interface defines the root (or trunk) of that structure.
 */
public interface Form {

    /**
     * Uniquely identifies the {@link Form}.  Consistent between different versions of the same
     * {@link Form}.
     */
    UUID getUuid();

    /**
     * Starts at <code>1</code> and increments every time a {@link Form} is updated.
     */
    int getVersion();

    /**
     * JSON Schema that defines and constrains the data (JSON) that may be submitted with the
     * {@link Form}.
     */
    JsonNode getSchema();

    /**
     * Optional metadata (JSON) associated with the {@link Form}.  May contain UI rendering hints.
     */
    JsonNode getMetadata();

}
