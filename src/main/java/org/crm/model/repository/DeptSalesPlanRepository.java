package org.crm.model.repository;

import org.crm.model.entity.DeptSalesPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DeptSalesPlanRepository extends JpaRepository<DeptSalesPlan, String>, JpaSpecificationExecutor<DeptSalesPlan> {

    @Query(value = "select * from dept_sales_plan where DATE_FORMAT(date, '%Y-%m') = ?1", nativeQuery = true)
    List<DeptSalesPlan> findByDate(String date);

}
