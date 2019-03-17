package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.model.entity.Role;
import org.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("")
    public Object list() {
        Object dataList = this.roleService.getList();
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Role role, String[] objects) {
        this.roleService.save(role, objects);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/{id}/objects", method = RequestMethod.GET)
    public Object setRoleObjects(@PathVariable("id") String id) {
        List<?> roleObjects = this.roleService.getObjects(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, roleObjects);
    }

    @RequestMapping(value = "/{id}/objects", method = RequestMethod.POST)
    public Object setRoleObjects(@PathVariable("id") String id, @RequestParam(name = "objectIds") String[] objectIds) {
        this.roleService.saveRoleObjects(id, objectIds);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") String id) {
        this.roleService.delete(id);
        return ResponseUtils.success();
    }

}
