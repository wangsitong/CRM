package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.model.dto.DirectSalesDTO;
import org.crm.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @RequestMapping("/sales-channel")
    public Object salesChannels(DirectSalesDTO condition) {
        List<?> dataList = this.analysisService.getDirectSalesChannelStatis(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/customer-sales-rank")
    public Object getCustomerSalesRank(DirectSalesDTO condition,
                                       @RequestParam(name = "len", required = false, defaultValue = "5") int len) {
        List<?> dataList = this.analysisService.getCustomerSalesRank(condition, len);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/salesCountPerMonth")
    public Object salesCountPerMonth(DirectSalesDTO condition) {
        List<?> dataList = this.analysisService.getSalesCountPerMonth(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

}
