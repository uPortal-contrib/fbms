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

public interface FormRepository extends CrudRepository<FormEntity,FormIdentifier> {

    @Query("SELECT COUNT(f) > 0 FROM FormEntity f WHERE f.id.fname = :fname")
    boolean existsByFname(@Param("fname") String fname);

    @Query("SELECT f FROM FormEntity f WHERE f.id.version = (SELECT MAX(b.id.version) from FormEntity b WHERE b.id.fname = :fname) AND f.id.fname = :fname")
    FormEntity findMostRecentByFname(@Param("fname") String fname);

}
