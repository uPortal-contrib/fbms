package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

/**
 * Represents the central abstraction of the FBMS component.  A Form is a collection of inputs and
 * metadata surrponding those inputs that can be solicited from a user through (at least) a web user
 * interface.
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
