package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.SalesPlan;
import org.crm.service.SalesPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/task/manager")
public class SalesPlanController {

    @Autowired
    private SalesPlanService salesPlanService;

    @RequestMapping("")
    public Object list(SalesPlan condition,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageDTO<?> dto = this.salesPlanService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(SalesPlan instance) {
        if (StringUtils.isBlank(instance.getId())) {
            if (this.salesPlanService.getTotal(instance) > 0) {
                return ResponseUtils.error("数据已存在");
            }
        }

        this.salesPlanService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam(name = "id", required = false) String id,
                         @RequestParam(name = "managerId", required = false) String managerId) {
        if (StringUtils.isNotBlank(id)) {
            this.salesPlanService.delete(id);
        }
        if (StringUtils.isNotBlank(managerId)) {
            this.salesPlanService.deleteByExecutorId(managerId);
        }
        return ResponseUtils.success();
    }

}
