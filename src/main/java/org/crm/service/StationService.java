package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.Station;
import org.crm.model.repository.StationRepository;
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
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public Page<Station> getList(Station condition, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return null;
            }
        };
        return this.stationRepository.findAll(specification, pageable);
    }

    public Station getByStationId(String stationId) {
        return this.stationRepository.findByStationId(stationId);
    }

    public Station getByName(String name) {
        return this.stationRepository.findByName(name);
    }

    @Transactional
    public void save(Station instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.stationRepository.save(instance);
    }

    @Transactional
    public void delete(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            if (StringUtils.isBlank(_id)) {
                continue;
            }
            this.stationRepository.deleteById(_id);
        }
    }

}
