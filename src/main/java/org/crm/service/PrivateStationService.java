package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.PrivateStation;
import org.crm.model.repository.PrivateStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class PrivateStationService {

    @Autowired
    private PrivateStationRepository privateStationRepository;

    public Page<PrivateStation> getList(PrivateStation condition, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return null;
            }
        };
        return this.privateStationRepository.findAll(specification, pageable);
    }

    public PrivateStation getByStationId(String stationId) {
        return this.privateStationRepository.findByStationId(stationId);
    }

    public PrivateStation getByName(String name) {
        return this.privateStationRepository.findByName(name);
    }

    @Transactional
    public void save(PrivateStation instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.privateStationRepository.save(instance);
    }

    @Transactional
    public void delete(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            if (StringUtils.isBlank(_id)) {
                continue;
            }
            this.privateStationRepository.deleteById(_id);
        }
    }

}
