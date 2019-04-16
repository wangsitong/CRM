package org.crm.controller;

import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.User;
import org.crm.service.UserRoleService;
import org.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping("")
    public Object list(User condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO pageDTO = this.userService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, pageDTO.getTotal());
        List<User> dataList = pageDTO.getDataList();
        dataList.forEach(data -> {
            List<?> roles = this.userRoleService.getRoles(data.getId());
            data.setRoles(roles);
        });

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList, pageInfo);
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

    @RequestMapping(value = "/{id}/roles")
    public Object roles(@PathVariable("id") String id) {
        List<?> dataList = this.userService.getRoles(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping(value = "/{id}/roles", method = RequestMethod.POST)
    public Object setRoles(@PathVariable("id") String id, @RequestParam(value = "roleId", required = false) String[] roleId) {
        this.userService.addRoles(id, roleId);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Object changePassword(@RequestParam("id") String id,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("password") String password) {
        User user = this.userService.getById(id);
        if (!oldPassword.equals(user.getPassword())) {
            return ResponseUtils.error("原始密码错误!");
        }
        user.setPassword(password);
        try {
            this.userService.save(user);
            return ResponseUtils.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtils.error("操作失败, 请重试!");
    }

}
