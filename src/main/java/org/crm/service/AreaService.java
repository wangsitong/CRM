package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.Area;
import org.crm.model.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public Page getList(Area condition, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Area> specification = (Specification<Area>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (condition != null) {
                if (StringUtils.isNotBlank(condition.getName())) {
                    Path<String> name = root.get("name");
                    predicates.add(criteriaBuilder.like(name, "%" + condition.getName() + "%"));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
        Page<Area> pageData = this.areaRepository.findAll(specification, pageable);
        return pageData;
    }

    public Area getByCode(String code) {
        return this.areaRepository.findByCode(code);
    }

    public Area getByName(String name) {
        return this.areaRepository.findByName(name);
    }

    public void save(Area area) {
        if (StringUtils.isBlank(area.getId())) {
            area.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.areaRepository.save(area);
    }

    public void delete(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            if (StringUtils.isBlank(_id)) {
                continue;
            }
            this.areaRepository.delete(this.areaRepository.findById(_id).get());
        }
    }

}
