package org.crm.service;

import org.crm.model.dto.DirectSalesDTO;
import org.crm.model.repository.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    public List<?> getDirectSalesChannelStatis(DirectSalesDTO condition) {
        return this.analysisRepository.findStatisBySaleChannel(condition);
    }

    public List<?> getCustomerSalesRank(DirectSalesDTO condition, int maxResults) {
        return this.analysisRepository.findCustomerSalesRank(condition, maxResults);
    }

    public List<?> getSalesCountPerMonth(DirectSalesDTO condition) {
        return this.analysisRepository.findSalesCountPerMonth(condition);
    }

}
