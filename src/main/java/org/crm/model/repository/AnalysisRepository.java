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

    List<Map<String, Object>> findByStationAndArea(SalesDTO condition, int firstResult, int maxResults);

    int findCountByStationsAndArea(SalesDTO condition);

    List<Map<String, Object>> findByManagerAndOilsCategory(SalesDTO condition);

    Map<String, Object> findBySalesCountRange(SalesDTO condition);

    List<Map<String, Object>> findByCustomerSalesCount(SalesDTO condition);

    List<Map<String, Object>> findByManagerAndOilsCategoryPerMonth(int year);

    List<Map<String, Object>> findBySalesArea(SalesDTO condition);

    List<Map<String, Object>> findBySelfSalesPerDays(SalesDTO condition);

    List<Map<String, Object>> findByCustomerDemandExecuteRate(SalesDTO condition);

    List<Map<String, Object>> findCustomerByLastSalesDate(int firstResult, int maxResults);

    int findCountByLastSalesDate();

    List<Map<String, Object>> findAreaDemand(int year);

}
