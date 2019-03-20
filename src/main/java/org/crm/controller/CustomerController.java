package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Customer;
import org.crm.model.entity.Manager;
import org.crm.service.CustomerService;
import org.crm.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("")
    public Object list(Customer condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageDTO pageDTO = this.customerService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, pageDTO.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, pageDTO.getDataList(), pageInfo);
    }

    @RequestMapping("/{id}")
    public Object info(@PathVariable("id") String id) {
        Object data = this.customerService.getById(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/exists")
    public Object exists(@RequestParam(name = "code", required = false) String code,
                         @RequestParam(name = "name", required = false) String name) {
        if (StringUtils.isNotBlank(code)) {
            Object data = this.customerService.getByCode(code);
            if (data != null) {
                return ResponseUtils.success();
            }
        }
        if (StringUtils.isNotBlank(name)) {
            Object data = this.customerService.getByName(name);
            if (data != null) {
                return ResponseUtils.success();
            }
        }
        return ResponseUtils.error("Code " + code + " has not exists.");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Customer instance) {
        try {
            this.customerService.save(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error(e.getMessage());
        }
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String id) {
        this.customerService.delete(id);
        return ResponseUtils.success();
    }

}
