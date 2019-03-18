package org.crm.model.repository;

import org.crm.model.entity.DirectSales;
import org.crm.model.entity.Sales;

import java.util.List;

public interface DirectSalesRepository {

    Sales findById(String id);

    List<?> findByCondition(DirectSales condition, int fistResult, int maxResults);

    int findCount(DirectSales condition);

    void save(DirectSales sales);

    void delete(String id);

}
