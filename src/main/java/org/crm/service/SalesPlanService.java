package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.model.entity.SalesPlan;
import org.crm.model.repository.SalesPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class SalesPlanService {

    @Autowired
    private SalesPlanRepository salesPlanRepository;

    public PageDTO<SalesPlan> getList(SalesPlan condition, int page, int pageSize) {
        int total = this.salesPlanRepository.findCount(condition);
        List<?> dataList = this.salesPlanRepository.find(condition, (page - 1) * pageSize, pageSize);

        PageDTO dto = new PageDTO(total, dataList);
        return dto;
    }

    @Transactional
    public void save(SalesPlan instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.salesPlanRepository.insert(instance);
        } else {
            this.salesPlanRepository.update(instance);
        }
    }

    @Transactional
    public void delete(String id) {
        this.salesPlanRepository.delete(id);
    }

    @Transactional
    public void deleteByExecutorId(String managerId) {
        this.salesPlanRepository.deleteByExecutorId(managerId);
    }

}
