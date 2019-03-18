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

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Object getList(Manager condition, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Manager> specification = new Specification<Manager>() {
            @Override
            public Predicate toPredicate(Root<Manager> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (condition != null) {
                    if (StringUtils.isNotBlank(condition.getName())) {
                        Path name = root.get("name");
                        predicate = criteriaBuilder.like(name, "%" + condition.getName() + "%");
                    }
                }
                return predicate;
            }
        };
        Page<Manager> pageData = this.managerRepository.findAll(specification, pageable);
        return pageData;
    }

}
