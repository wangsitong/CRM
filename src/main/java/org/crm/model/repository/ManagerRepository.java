package org.crm.model.repository;

import org.crm.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ManagerRepository extends JpaSpecificationExecutor<Manager>, PagingAndSortingRepository<Manager, String> {

    List<?> findByManagerId(String managerId);

}
