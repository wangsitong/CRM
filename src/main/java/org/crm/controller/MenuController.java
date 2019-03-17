package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.crm.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("")
    public Object menus() {
        Object menus = this.menuService.getMenus();
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, menus);
    }

}
