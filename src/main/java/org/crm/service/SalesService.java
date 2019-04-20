package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Sales;
import org.crm.model.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public Sales getById(String id) {
        return this.salesRepository.findById(id);
    }

    public List<Sales> getByNeedTransfer(SalesDTO condition) {
        return this.salesRepository.findByNeedTransfer(condition);
    }

    @Transactional
    public List<Sales> saveImportDatas(List<Sales> dataList) {
        List<Sales> impDatas = new ArrayList<>();
        for (Sales sales : dataList) {
            List<Sales> list = this.salesRepository.findExact(sales);
            if (list != null && !list.isEmpty()) {
                continue;
            }

            // sales.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.salesRepository.save(sales);
            impDatas.add(sales);
        }
        return impDatas;
    }

    @Transactional
    public void saveOrUpdate(List<Sales> dataList) {
        for (Sales sales : dataList) {
            if (StringUtils.isBlank(sales.getId())) {
                sales.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            try {
                this.salesRepository.merge(sales);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Transactional
    public void setTransfer(String id, String managerId, String managerName) {
        Sales sales = this.salesRepository.findById(id);
        sales.setTransfer("1");
        sales.setOriginalManagerId(managerId);
        sales.setOriginalManagerName(managerName);
        this.salesRepository.save(sales);
    }

    @Transactional
    public void removeTransfer(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            Sales sales = this.salesRepository.findById(_id);
            sales.setTransfer(null);
            sales.setOriginalManagerId(null);
            sales.setOriginalManagerName(null);
            this.salesRepository.save(sales);
        }
    }

}
