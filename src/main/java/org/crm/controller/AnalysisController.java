package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.service.AnalysisService;
import org.crm.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/business/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private DemandService demandService;

    @RequestMapping("/salesChannel")
    public Object salesChannels(SalesDTO condition) {
        List<?> dataList = this.analysisService.getDirectSalesChannelStatis(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/customer-sales-rank")
    public Object getCustomerSalesRank(SalesDTO condition,
                                       @RequestParam(name = "len", required = false, defaultValue = "5") int len) {
        List<?> dataList = this.analysisService.getCustomerSalesRank(condition, len);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/salesCountPerMonth")
    public Object salesCountPerMonth(SalesDTO condition) {
        List<?> dataList = this.analysisService.getSalesCountPerMonth(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/salesArea")
    public Object salesArea(@RequestParam("date") String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date d = df.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        List<Map<String, Object>> demands = this.demandService.getAreas(c.get(Calendar.YEAR) + "");

        List<Map<String, Object>> dataList = this.analysisService.findManagerSales(d);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        dataList.forEach((item)-> {
            String area = item.get("customer_area").toString();
            Map<String, Object> datas = (Map<String, Object>) resultMap.get(area);
            if (datas == null) {
                datas = new HashMap<>();
                datas.put("area", area);
                datas.put("sales", new ArrayList<>());

                resultMap.put(area, datas);
            }
            List sales = (List) datas.get("sales");
            sales.add(item);
        });

        demands.forEach((item) -> {
            String area = item.get("area").toString();
            Map<String, Object> datas = (Map<String, Object>) resultMap.get(area);
            if (datas == null) {
                datas = new HashMap<>();
                datas.put("area", area);
                resultMap.put(area, datas);
            }
            datas.put("demand", item);
        });

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, resultMap.values());
    }

}
