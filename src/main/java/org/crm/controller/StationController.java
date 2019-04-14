package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Station;
import org.crm.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/station/internal")
public class StationController {

    @Autowired
    private StationService stationService;

    @RequestMapping("")
    public Object list(Station condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Page<Station> pageData = this.stationService.getList(condition, page - 1, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, (int)pageData.getTotalElements());

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, pageData.getContent(), pageInfo);
    }

    @RequestMapping("/all")
    public Object listAll(Station condition) {
        Object data = this.stationService.getAll(condition);

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping("/get")
    public Object exists(@RequestParam(name = "stationId", required = false) String stationId,
                         @RequestParam(name = "name", required = false) String name) {
        Station data = null;
        if (StringUtils.isNotBlank(stationId)) {
            data = this.stationService.getByStationId(stationId);
        } else if (StringUtils.isNotBlank(name)) {
            data = this.stationService.getByName(name);
        }
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, data);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Station instance) {
        this.stationService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") String id) {
        this.stationService.delete(id);
        return ResponseUtils.success();
    }

}
