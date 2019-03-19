package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageInfo;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Area;
import org.crm.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping("")
    public Object list(Area condition,
                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Page result = this.areaService.getList(condition, page - 1, pageSize);
        PageInfo pageInfo = new PageInfo(page, pageSize, (int)result.getTotalElements());
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, result.getContent(), pageInfo);
    }

    @RequestMapping("/validate")
    public Object validate(@RequestParam(name = "code", required = false) String code,
                           @RequestParam(name = "name", required = false) String name) {
        List<String> messages = new ArrayList<>();
        if (StringUtils.isNotBlank(code)) {
            Area area = this.areaService.getByCode(code);
            if (area != null) {
                messages.add("编号已存在");
            }
        }
        if (StringUtils.isNotBlank(name)) {
            Area area = this.areaService.getByName(code);
            if (area != null) {
                messages.add("名称已存在");
            }
        }
        if (messages.isEmpty()) {
            return ResponseUtils.success();
        } else {
            return ResponseUtils.error(StringUtils.join(messages, ","));
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Area instance) {
        this.areaService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String id) {
        this.areaService.delete(id);
        return ResponseUtils.success();
    }

}
