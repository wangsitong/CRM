package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.ManagerTask;
import org.crm.service.ManagerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/task/manager")
public class ManagerTaskController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @RequestMapping("")
    public Object list(ManagerTask condition,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageDTO<?> dto = this.managerTaskService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(ManagerTask instance) {
        this.managerTaskService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam(name = "id", required = false) String id,
                         @RequestParam(name = "managerId", required = false) String managerId) {
        if (StringUtils.isNotBlank(id)) {
            this.managerTaskService.delete(id);
        }
        if (StringUtils.isNotBlank(managerId)) {
            this.managerTaskService.deleteByManagerId(managerId);
        }
        return ResponseUtils.success();
    }

}
