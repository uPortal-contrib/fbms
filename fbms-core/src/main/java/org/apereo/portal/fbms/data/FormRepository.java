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
