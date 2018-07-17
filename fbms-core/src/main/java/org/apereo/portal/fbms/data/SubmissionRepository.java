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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository extends CrudRepository<SubmissionEntity,SubmissionIdentifier> {

    @Query("SELECT s FROM SubmissionEntity s " +
            "WHERE s.id.username = :username " +
            "AND s.id.fname = :fname " +
            "ORDER BY s.id.timestamp DESC")
    List<SubmissionEntity> findByUsernameAndFname(@Param("username") String username, @Param("fname") String fname);

    @Query("SELECT s FROM SubmissionEntity s " +
            "WHERE s.id.timestamp = (SELECT MAX(b.id.timestamp) from SubmissionEntity b WHERE b.id.username = :username AND b.id.fname = :fname) " +
            "AND s.id.username = :username " +
            "AND s.id.fname = :fname")
    SubmissionEntity findMostRecentByUsernameAndFname(@Param("username") String username, @Param("fname") String fname);

}
