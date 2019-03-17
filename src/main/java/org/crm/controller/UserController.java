package org.crm.controller;

import org.crm.common.PageDTO;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.User;
import org.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public Object list(User condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO pageDTO = this.userService.getList(condition, page, pageSize);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, pageDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getById(@PathVariable("id") String id) {
        User user = this.userService.getById(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, user);
    }

    @RequestMapping(value = "", method = { RequestMethod.POST, RequestMethod.PUT })
    public Object save(User user) {
        try {
            this.userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error(e.getMessage());
        }
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") String id) {
        String[] ids = id.split(",");
        this.userService.delete(ids);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/{id}/role")
    public Object roles(@PathVariable("id") String id) {
        List<?> dataList = this.userService.getRoles(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping(value = "/{id}/role/objects")
    public Object roleObjects(@PathVariable("id") String id) {
        List<?> dataList = this.userService.getRoleObjects(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

}
