package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.Station;
import org.crm.model.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public Page<Station> getList(Station condition, int page, int pageSize) {
        return this.stationRepository.findAll(this.createSpecification(condition), PageRequest.of(page, pageSize));
    }

    public List<Station> getAll(Station condition) {
        return this.stationRepository.findAll(this.createSpecification(condition), Sort.by(Sort.Order.asc("name")));
    }

    private Specification<Station> createSpecification(Station condition) {
        Specification<Station> spec = (Specification<Station>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (condition != null) {
                if (StringUtils.isNotBlank(condition.getStationId())) {
                    predicates.add(criteriaBuilder.equal(root.get("stationId"), condition.getStationId()));
                }
                if (StringUtils.isNotBlank(condition.getTransfer())) {
                    predicates.add(criteriaBuilder.equal(root.get("transfer"), condition.getTransfer()));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
        return spec;
    }

    public Station getByStationId(String stationId) {
        Station condition = new Station();
        condition.setStationId(stationId);
        Example<Station> example = Example.of(condition);
        Optional<Station> data = this.stationRepository.findOne(example);
        return data.get();
    }

    public Station getByName(String name) {
        Station condition = new Station();
        condition.setName(name);
        Example<Station> example = Example.of(condition);
        Optional<Station> data = this.stationRepository.findOne(example);
        return data.get();
    }

    public void save(Station instance) {
        if (StringUtils.isBlank(instance.getId())) {
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.stationRepository.save(instance);
    }

    public void  delete(String id) {
        this.stationRepository.deleteById(id);
    }

}
