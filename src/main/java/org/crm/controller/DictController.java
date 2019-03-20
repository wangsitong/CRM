package org.crm.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.crm.common.ConstantUtils;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.Dict;
import org.crm.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping("/{id}/children")
    public Object children(@PathVariable("id") String id) {
        List<Dict> dataList = this.dictService.getChildren(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, dataList);
    }

    @RequestMapping("/{id}/children/maxKey")
    public Object maxKey(@PathVariable("id") String id) {
        Object value = this.dictService.getMaxKey(id);
        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, value);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(Dict instance) {
        if (StringUtils.isBlank(instance.getKey())) {
            String max = this.dictService.getMaxKey(instance.getParent());
            if (max == null) {
                if (ConstantUtils.DICT_ROOT_KEY.equals(instance.getParent())) {
                    max = this.dictService.getMaxKey(ConstantUtils.DICT_ROOT_KEY);
                    max = String.valueOf(NumberUtils.toInt(max) + 10);
                } else {
                    max = instance.getParent() + "0";
                }
            } else {
                if (ConstantUtils.DICT_ROOT_KEY.equals(instance.getParent())) {
                    max = String.valueOf(NumberUtils.toInt(max) + 10);
                } else {
                    max = String.valueOf(NumberUtils.toInt(max) + 1);
                }
            }
            instance.setKey(max);
        }
        this.dictService.save(instance);
        return ResponseUtils.success();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Object delete(String id) {
        this.dictService.delete(id);
        return ResponseUtils.success();
    }

}
