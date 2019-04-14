package org.crm.model.repository;

import org.crm.model.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StationRepository extends JpaRepository<Station, String>, JpaSpecificationExecutor<Station> {
}
