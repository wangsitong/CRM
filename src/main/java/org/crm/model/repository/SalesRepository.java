package org.crm.model.repository;

import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Sales;

import java.util.List;

public interface SalesRepository {

    Sales findById(String id);

    List<?> findByCondition(SalesDTO condition, int firstResult, int maxResults);

    int findCount(SalesDTO condition);

    List<Sales> findByManagerSales(SalesDTO condition, int firstResult, int maxResults);

    int findCountByManagerSales(SalesDTO condition);

    List<Sales> findExact(Sales condition);

    List<Sales> findByNeedTransfer(SalesDTO condition);

    void save(Sales sales);

    void merge(Sales sales);

    void delete(String id);

}
