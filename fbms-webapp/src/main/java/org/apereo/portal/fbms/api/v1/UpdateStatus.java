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

    public static UpdateStatus success(List<String> messages) {
        return new UpdateStatus(true, messages);
    }

    public static UpdateStatus failure(String message) {
        return new UpdateStatus(false, Collections.singletonList(message));
    }

    public static UpdateStatus failure(List<String> messages) {
        return new UpdateStatus(false, messages);
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
