package org.crm.controller;

import org.crm.common.ResponseUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @RequestMapping("/test")
    public Object test() {
        System.out.println((new Date()).toString());
        return ResponseUtils.success();
    }

}
