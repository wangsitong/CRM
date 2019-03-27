package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Station;
import org.crm.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @RequestMapping("")
    public Object list(Station condition,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<Station> pages = this.stationService.getList(condition, page, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, (int)pages.getTotalElements());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, pages.getContent(), pageInfo);
    }

    @RequestMapping("/validate")
    public Object validate(@RequestParam(name = "stationId", required = false) String stationId,
                           @RequestParam(name = "name", required = false) String name) {
        Object data = null;
        if (StringUtils.isNotBlank(stationId)) {
            data = this.stationService.getByStationId(stationId);
            return ResponseUtils.success();
        }
        if (StringUtils.isNotBlank(name)) {
            data = this.stationService.getByName(name);
            return ResponseUtils.success();
        }
        return ResponseUtils.error("data is not exists");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Station instance) {
        this.stationService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(String id) {
        this.stationService.delete(id);
        return ResponseUtils.success();
    }

}
