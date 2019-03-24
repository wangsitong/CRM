package org.crm.service;

import org.crm.model.repository.DemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemandService {

    @Autowired
    private DemandRepository demandRepository;

    public List<Map<String, Object>> getAreas(String year) {
        return this.demandRepository.getAreas(year);
    }

}
