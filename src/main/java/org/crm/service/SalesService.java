package org.crm.service;

import org.crm.common.PageDTO;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Sales;
import org.crm.model.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public PageDTO<Sales> getList(SalesDTO condition, int firstResult, int maxResults) {
        int total = this.salesRepository.findCount(condition);
        List<?> dataList = this.salesRepository.findByCondition(condition, firstResult, maxResults);

        PageDTO dto = new PageDTO(total, dataList);
        return dto;
    }

}
