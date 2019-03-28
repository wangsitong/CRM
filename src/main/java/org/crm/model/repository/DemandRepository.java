package org.crm.model.repository;

import org.crm.model.entity.Demand;

import java.util.List;
import java.util.Map;

public interface DemandRepository {

    List<Demand> findAll(Demand condition, int firstResult, int maxResults);

    int findCount(Demand condition);

    List<Map<String, Object>> getAreas(String year);

    Map<String, Object> findTotal(String year);

    void save(Demand instance);

    void update(Demand instance);

    void delete(String id);

    void deleteByCustomer(String customerId);

}
