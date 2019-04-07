package org.crm.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.SalesPlan;
import org.crm.service.AnalysisService;
import org.crm.service.DemandService;
import org.crm.service.SalesPlanService;
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
    @Autowired
    private SalesPlanService salesPlanService;

    @RequestMapping("/salesCount")
    public Object salesCount(SalesDTO condition) {
        double value = this.analysisService.getSalesCount(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, value);
    }

    @RequestMapping("/salesCount/retail")
    public Object retailSalesCount(SalesDTO condition) {
        condition.setSalesStationNotEquals("#");
        double value = this.analysisService.getSalesCount(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, value);
    }

    @RequestMapping("/sales/total")
    public Object salesChannels(SalesDTO condition) {
        condition.setTransfer("1");
        Map<String, ?> dataMap = this.analysisService.getSalesTotal(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataMap);
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

    @RequestMapping("/sales/manager")
    public Object managerSales(SalesDTO condition) {
        List<?> dataList = this.analysisService.getManagerSales(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    /*@RequestMapping("/salesArea")
    public Object salesArea(@RequestParam("date") String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date d = df.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        List<Map<String, Object>> demands = this.demandService.getAreas(c.get(Calendar.YEAR) + "");

        List<Map<String, Object>> dataList = this.analysisService.getManagerSales(null);

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
    }*/

    @RequestMapping("/sales/area")
    public Object salesArea(SalesDTO condition) {
        Object data = this.analysisService.getBySalesArea(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/demand")
    public Object demandAll(@RequestParam("year") String year) {
        Object data = this.demandService.getTotal(year);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/managerCompleteRank")
    public Object managerCompleteRank(String date) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date d = df.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        Date startDate = c.getTime();
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = c.getTime();

        SalesDTO condition = new SalesDTO();
        condition.setStartSalesDate(startDate);
        condition.setEndSalesDate(endDate);

        List<Map<String, Object>> dataList = this.analysisService.getManagerSales(condition);

        for (Map<String, Object> data : dataList) {
            String managerId = data.get("managerId").toString();
            SalesPlan taskCondition = new SalesPlan();
            taskCondition.setExecutorId(managerId);
            taskCondition.setDate(d);
            PageDTO dto = this.salesPlanService.getList(taskCondition, 1, 10);
            List<SalesPlan> managerTasks = dto.getDataList();
            if (managerTasks != null && managerTasks.size() > 0) {
                Map<String, String> task = BeanUtils.describe(managerTasks.get(0));
                data.putAll(task);
            }
        }

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/sales/stationsAndArea")
    public Object getByStationAndArea(SalesDTO condition,
                                      @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO dto = this.analysisService.getByStationAndArea(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping("/sales/managerAndOilsCategory")
    public Object getByManagerAndOilsCategory(SalesDTO condition) {
        Object data = this.analysisService.getByManagerAndOilsCategory(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/salesCountRange")
    public Object getBySalesCountRange(SalesDTO condition) {
        Object data = this.analysisService.getBySalesCountRange(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/customerSalesCount")
    public Object getByCustomerSalesCount(SalesDTO condition) {
        Object data = this.analysisService.getByCustomerSalesCount(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/managerAndOilsCategoryPerMonth")
    public Object getByManagerAndOilsCategoryPerMonth(@RequestParam("year") int year) {
        Object data = this.analysisService.getByManagerAndOilsCategoryPerMonth(year);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/selfSalesPerDays")
    public Object getBySelfSalesPerDays(SalesDTO condition) {
        Object data = this.analysisService.getBySelfSalesPerDays(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/customerDemandExecuteRate")
    public Object getByCustomerDemandExecuteRate(SalesDTO condition) {
        Object data = this.analysisService.getByCustomerDemandExecuteRate(condition);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/sales/customer/alarm")
    public Object customerSalesAlarm(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO<?> dto = this.analysisService.getCustomerByLastSalesDate(page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

}
