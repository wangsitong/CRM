package org.crm.model.repository;

import org.crm.model.entity.PrivateStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PrivateStationRepository extends JpaRepository<PrivateStation, String>, JpaSpecificationExecutor<PrivateStation> {

    PrivateStation findByStationId(String stationId);

    PrivateStation findByName(String name);

}
