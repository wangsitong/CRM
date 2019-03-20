package org.crm.service;

import org.crm.model.entity.Dict;
import org.crm.model.repository.DictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DictService {

    @Autowired
    private DictRepository dictRepository;

    public List<Dict> getChildren(String parent) {
        return this.dictRepository.findByParentOrderBySortNoAsc(parent);
    }

    public String getMaxKey(String parent) {
        return this.dictRepository.findMaxKey(parent);
    }

    @Transactional
    public void save(Dict instance) {
        this.dictRepository.save(instance);
    }

    @Transactional
    public void delete(String id) {
        this.dictRepository.deleteByParent(id);
        this.dictRepository.deleteById(id);
    }

}
