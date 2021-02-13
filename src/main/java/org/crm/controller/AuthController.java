package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.model.entity.User;
import org.crm.service.MenuService;
import org.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @RequestMapping("/login")
    public Object login(User user) {
        User u = this.userService.getByUserId(user.getUserId());
        if (u == null) {
            return ResponseUtils.error("用户名或密码错误!");
        }
        if (!user.getPassword().equals(u.getPassword())) {
            return ResponseUtils.error("用户名或密码错误!");
        }
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, u);
    }

    @RequestMapping("/menus")
    public Object authMenus() {
        Object menus = this.menuService.getMenus();
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, menus);
    }

}
