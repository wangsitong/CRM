package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.Manager;
import org.crm.model.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Page getList(Manager condition, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Manager> specification = (Specification<Manager>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (condition != null) {
                if (StringUtils.isNotBlank(condition.getName())) {
                    Path<String> name = root.get("name");
                    predicates.add(criteriaBuilder.like(name, "%" + condition.getName() + "%"));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
        Page<Manager> pageData = this.managerRepository.findAll(specification, pageable);
        return pageData;
    }

    public Manager getByManagerId(String managerId) {
        List<?> dataList = this.managerRepository.findByManagerId(managerId);
        if (dataList != null && !dataList.isEmpty()) {
            return (Manager) dataList.get(0);
        }
        return null;
    }

    public void save(Manager manager) {
        if (StringUtils.isBlank(manager.getId())) {
            manager.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.managerRepository.save(manager);
    }

    public void delete(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            if (StringUtils.isBlank(_id)) {
                continue;
            }
            this.managerRepository.delete(this.managerRepository.findById(_id).get());
        }
    }

}
