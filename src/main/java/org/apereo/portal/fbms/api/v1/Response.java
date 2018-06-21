package org.apereo.portal.fbms.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.UUID;

/**
 * Represents an individual user's response to a specific {@link Form}.
 */
public interface Response {

    /**
     * The username of the user who produced this Response.
     */
    String getUsername();

    /**
     * The UUID of the {@link Form} to which this Response applies.
     */
    UUID getFormUuid();

    /**
     * The version number of the {@link Form} at the time this Response was made.
     */
    int getFormVersion();

    /**
     * The moment at which this response was created.
     */
    Date getTimestamp();

    /**
     * JSON representing the user's answers to the questions contained in the {@link Form}.
     */
    JsonNode getAnswers();

}
