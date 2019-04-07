package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.common.ResponseUtils;
import org.crm.model.entity.DeptSalesPlan;
import org.crm.model.repository.DeptSalesPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DeptSalesPlanService {

    @Autowired
    private DeptSalesPlanRepository deptSalesPlanRepository;

    public PageDTO<?> getList(DeptSalesPlan condition, int page, int pageSize) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
        Page<DeptSalesPlan> data = this.deptSalesPlanRepository.findAll(specification, PageRequest.of(page - 1, pageSize));

        return new PageDTO<>((int)data.getTotalElements(), data.getContent());
    }

    public DeptSalesPlan getByDate(String date) {
        List<DeptSalesPlan> dataList = this.deptSalesPlanRepository.findByDate(date);
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }

    @Transactional
    public void save(DeptSalesPlan instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));

        }
        this.deptSalesPlanRepository.save(instance);
    }

    @Transactional
    public void delete(String id) {
        this.deptSalesPlanRepository.deleteById(id);
    }

}
