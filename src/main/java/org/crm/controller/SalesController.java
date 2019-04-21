package org.crm.controller;

import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Customer;
import org.crm.model.entity.Sales;
import org.crm.service.CustomerService;
import org.crm.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("")
    public Object list(SalesDTO condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO dto = this.salesService.getList(condition, (page - 1) * pageSize, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping("/tywy")
    public Object listOfTywy(SalesDTO condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        condition.setSalesStationNotEquals("#");
        condition.setSalesChannel("分销");

        PageDTO dto = this.salesService.getList(condition, (page - 1) * pageSize, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping("/manager")
    public Object managerSales(SalesDTO condition,
                               @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO dto = this.salesService.getManagerSales(condition, (page - 1) * pageSize, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping("/setTransfer")
    public Object setTransfer(@RequestParam("id") String id, @RequestParam("managerId") String managerId, @RequestParam("managerName") String managerName) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            this.salesService.setTransfer(_id, managerId, managerName);
        }

        return ResponseUtils.success();
    }

    @RequestMapping("/removeTransfer")
    public Object removeTransfer(String id) {
        this.salesService.removeTransfer(id);
        return ResponseUtils.success();
    }

}
