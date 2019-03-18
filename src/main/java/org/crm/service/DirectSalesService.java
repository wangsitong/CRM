package org.crm.service;

import org.crm.common.PageDTO;
import org.crm.model.entity.DirectSales;
import org.crm.model.entity.Sales;
import org.crm.model.repository.DirectSalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectSalesService {

    @Autowired
    private DirectSalesRepository directSalesRepository;

    public PageDTO<Sales> getList(DirectSales condition, int firstResult, int maxResults) {
        int total = this.directSalesRepository.findCount(condition);
        List<?> dataList = this.directSalesRepository.findByCondition(condition, firstResult, maxResults);

        PageDTO dto = new PageDTO(total, dataList);
        return dto;
    }

}
