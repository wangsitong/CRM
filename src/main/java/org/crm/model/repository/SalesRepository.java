package org.crm.model.repository;

import org.crm.model.entity.Sales;

import java.util.List;

public interface SalesRepository {

    Sales findById(String id);

    List<?> findByCondition(Sales condition, int fistResult, int maxResults);

    int findCount(Sales condition);

    void save(Sales sales);

    void delete(String id);

}
