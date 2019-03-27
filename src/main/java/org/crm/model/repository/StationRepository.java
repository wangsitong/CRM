package org.crm.model.repository;

import org.crm.model.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StationRepository extends JpaRepository<Station, String>, JpaSpecificationExecutor<Station> {

    Station findByStationId(String stationId);

    Station findByName(String name);

}
