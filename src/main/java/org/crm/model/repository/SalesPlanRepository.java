package org.crm.model.repository;

import org.crm.model.entity.SalesPlan;

import java.util.List;

public interface SalesPlanRepository {

    List<SalesPlan> find(SalesPlan condition, int firstResult, int maxResults);

    int findCount(SalesPlan condition);

    void insert(SalesPlan instance);

    void update(SalesPlan instance);

    void delete(String id);

    void deleteByExecutorId(String executorId);

}
