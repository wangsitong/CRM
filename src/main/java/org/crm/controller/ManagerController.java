package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.model.entity.Manager;
import org.crm.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @RequestMapping("")
    public Object list(Manager condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Page result = this.managerService.getList(condition, page - 1, pageSize);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, result);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Manager manager) {
        this.managerService.save(manager);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String id) {
        this.managerService.delete(id);
        return ResponseUtils.success();
    }

}
