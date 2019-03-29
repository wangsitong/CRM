package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.model.entity.ManagerTask;
import org.crm.model.repository.ManagerTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerTaskService {

    @Autowired
    private ManagerTaskRepository managerTaskRepository;

    public PageDTO<ManagerTask> getList(ManagerTask condition, int page, int pageSize) {
        int total = this.managerTaskRepository.findCount(condition);
        List<?> dataList = this.managerTaskRepository.find(condition, (page - 1) * pageSize, pageSize);

        PageDTO dto = new PageDTO(total, dataList);
        return dto;
    }

    @Transactional
    public void save(ManagerTask instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.managerTaskRepository.insert(instance);
        } else {
            this.managerTaskRepository.update(instance);
        }
    }

    @Transactional
    public void delete(String id) {
        this.managerTaskRepository.delete(id);
    }

    @Transactional
    public void deleteByManagerId(String managerId) {
        this.managerTaskRepository.deleteByManagerId(managerId);
    }

}
