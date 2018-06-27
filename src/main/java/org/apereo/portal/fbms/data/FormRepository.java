package org.apereo.portal.fbms.data;

import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<FormEntity,Long> {

    boolean existsByFname(String fname);

    FormEntity findByFname(String fname);

}
