package org.crm.service;

import org.crm.model.dto.SalesDTO;
import org.crm.model.repository.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    public List<?> getDirectSalesChannelStatis(SalesDTO condition) {
        return this.analysisRepository.findStatisBySaleChannel(condition);
    }

    public List<?> getCustomerSalesRank(SalesDTO condition, int maxResults) {
        return this.analysisRepository.findCustomerSalesRank(condition, maxResults);
    }

    public List<?> getSalesCountPerMonth(SalesDTO condition) {
        return this.analysisRepository.findSalesCountPerMonth(condition);
    }

    public List<Map<String, Object>> findManagerSales(Date date) {
        return this.analysisRepository.findManagerSales(date);
    }

}
