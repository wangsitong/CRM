package org.crm.model.repository;

import org.crm.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {

    Area findByCode(String code);

    Area findByName(String name);

}
