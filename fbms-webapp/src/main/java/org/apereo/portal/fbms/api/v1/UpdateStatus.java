package org.apereo.portal.fbms.api.v1;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * This class encapsulates the information that is returned (in the form of JSON) to a client that
 * sends an HTTP POST or a PUT.
 */
public class UpdateStatus implements Serializable {

    private final boolean success;
    private final List<String> messages;

    public UpdateStatus(boolean success, List<String> messages) {
        this.success = success;
        this.messages = messages;
    }

    public static UpdateStatus success(String message) {
        return new UpdateStatus(true, Collections.singletonList(message));
    }

    public static UpdateStatus failure(String message) {
        return new UpdateStatus(false, Collections.singletonList(message));
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "UpdateStatus{" +
                "success=" + success +
                ", messages=" + messages +
                '}';
    }

}
