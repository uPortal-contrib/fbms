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
package org.apereo.portal.fbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:fbms.properties"), // Must not be application.properties (https://stackoverflow.com/questions/42339226/spring-propertysources-value-not-overriding)
		@PropertySource(value = "file:${portal.home}/global.properties", ignoreResourceNotFound = true), // Higher-priority
		@PropertySource(value = "file:${portal.home}/fbms.properties", ignoreResourceNotFound = true) // Highest-priority
})
public class ApereoFbmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApereoFbmsApplication.class, args);
	}
}
