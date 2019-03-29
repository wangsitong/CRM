package org.crm.model.repository;

import org.crm.model.dto.SalesDTO;

import java.util.List;
import java.util.Map;

public interface AnalysisRepository {

    double findSalesCount(SalesDTO condition);

    List<Map<String, Object>> findStatisBySaleChannel(SalesDTO condition);

    List<Map<String, Object>> findCustomerSalesRank(SalesDTO condition, int maxResults);

    List<Map<String, Object>> findSalesCountPerMonth(SalesDTO condition);

    List<Map<String, Object>> findManagerSales(SalesDTO condition);

}
