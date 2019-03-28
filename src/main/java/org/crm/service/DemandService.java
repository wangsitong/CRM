package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.model.entity.Demand;
import org.crm.model.repository.DemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DemandService {

    @Autowired
    private DemandRepository demandRepository;

    public List<Map<String, Object>> getAreas(String year) {
        return this.demandRepository.getAreas(year);
    }

    public Map<String, Object> getTotal(String year) {
        return this.demandRepository.findTotal(year);
    }

    public PageDTO<Demand> getList(Demand condition, int page, int pageSize) {
        int total = this.demandRepository.findCount(condition);
        List<?> dataList = this.demandRepository.findAll(condition, (page - 1) * pageSize, pageSize);
        PageDTO dto = new PageDTO(total, dataList);
        return dto;
    }

    @Transactional
    public void save(Demand instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.demandRepository.save(instance);
        } else {
            this.demandRepository.update(instance);
        }
    }

    @Transactional
    public void delete(String id) {
        this.demandRepository.delete(id);
    }

    @Transactional
    public void deleteByCustomer(String customerId) {
        this.demandRepository.deleteByCustomer(customerId);
    }

}
