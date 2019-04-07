package org.crm.controller;


import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.DeptSalesPlan;
import org.crm.service.DeptSalesPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/business/task/manager/dept")
public class DeptSalesPlanController {

    @Autowired
    private DeptSalesPlanService deptSalesPlanService;

    @RequestMapping("")
    public Object list(DeptSalesPlan condition,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageDTO dto = this.deptSalesPlanService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping("/byDate")
    public Object getPlan(String date) {
        DeptSalesPlan data = this.deptSalesPlanService.getByDate(date);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(DeptSalesPlan instance) {
        this.deptSalesPlanService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            this.deptSalesPlanService.delete(id);
        }
        return ResponseUtils.success();
    }

}
