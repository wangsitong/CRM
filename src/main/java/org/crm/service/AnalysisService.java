package org.crm.service;

import org.crm.model.dto.SalesDTO;
import org.crm.model.repository.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    public Map<String, Double> getSalesTotal(SalesDTO condition) {
        List<Map<String, Object>> dataMap = this.analysisRepository.findStatisBySaleChannel(condition);
        double totalOfTransfer = this.analysisRepository.findSalesCount(condition);

        Map<String, Double> result = new HashMap<>();
        result.put("totalOfTransfer", totalOfTransfer);

        for (Map<String, Object> data : dataMap) {
            if ("直销".equals(data.get("channel"))) {
                result.put("totalOfDirect", (double)data.get("count"));
            } else if ("分销".equals(data.get("channel"))) {
                result.put("totalOfDist", (double)data.get("count"));
            }
        }
        return result;
    }

    public double getSalesCount(SalesDTO condition) {
        return this.analysisRepository.findSalesCount(condition);
    }

    public List<?> getDirectSalesChannelStatis(SalesDTO condition) {
        return this.analysisRepository.findStatisBySaleChannel(condition);
    }

    public List<?> getCustomerSalesRank(SalesDTO condition, int maxResults) {
        return this.analysisRepository.findCustomerSalesRank(condition, maxResults);
    }

    public List<?> getSalesCountPerMonth(SalesDTO condition) {
        return this.analysisRepository.findSalesCountPerMonth(condition);
    }

    public List<Map<String, Object>> getManagerSales(SalesDTO condition) {
        return this.analysisRepository.findManagerSales(condition);
    }

}
