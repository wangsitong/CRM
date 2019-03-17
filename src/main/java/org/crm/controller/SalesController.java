package org.crm.controller;

import org.crm.common.PageDTO;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Sales;
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

    @RequestMapping("")
    public Object list(Sales condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO dto = this.salesService.getList(condition, (page - 1) * pageSize, pageSize);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto);
    }

}
