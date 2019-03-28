package org.crm.controller;


import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Demand;
import org.crm.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/demand")
public class DemandController {

    @Autowired
    private DemandService demandService;

    @RequestMapping("")
    public Object list(Demand condition,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageDTO dto = this.demandService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, dto.getTotal());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dto.getDataList(), pageInfo);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Demand instance) {
        this.demandService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String id, @RequestParam(name = "customerId", required = false) String customerId) {
        if (StringUtils.isNotBlank(id)) {
            this.demandService.delete(id);
        }
        if (StringUtils.isNotBlank(customerId)) {
            this.demandService.deleteByCustomer(customerId);
        }
        return ResponseUtils.success();
    }

}
