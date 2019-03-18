package org.crm.model.repository;

import org.crm.model.dto.DirectSalesDTO;

import java.util.List;
import java.util.Map;

public interface AnalysisRepository {

    List<Map<String, Object>> findStatisBySaleChannel(DirectSalesDTO condition);

    List<Map<String, Object>> findCustomerSalesRank(DirectSalesDTO condition, int maxResults);

    List<Map<String, Object>> findSalesCountPerMonth(DirectSalesDTO condition);

}
