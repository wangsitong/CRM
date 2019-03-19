package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.crm.common.PageDTO;
import org.crm.model.entity.Customer;
import org.crm.model.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository<Customer> customerRepository;

    public Customer getByCode(String code) {
        return this.customerRepository.findByCode(code);
    }

    public Customer getById(String id) {
        return this.customerRepository.findById(id);
    }

    public PageDTO<Customer> getList(Customer condition, int page, int pageSize) {
        int total = this.customerRepository.findCount(condition);
        List<?> dataList = this.customerRepository.findList(condition, (page - 1) * pageSize, pageSize);

        return new PageDTO(total, dataList);
    }

    @Transactional
    public void save(Customer instance) throws Exception {
        if (StringUtils.isBlank(instance.getId())) {
            T u = this.customerRepository.findByCode(instance.getCode());
            if (u != null) {
                throw new Exception("Customer('" + instance.getCode() + "') exists");
            }
            instance.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.customerRepository.insert(instance);
        } else {
            this.customerRepository.update(instance);
        }
    }

    @Transactional
    public void delete(String id) {
        String[] ids = id.split(",");
        for (String _id : ids) {
            if (StringUtils.isBlank(_id)) {
                continue;
            }
            this.customerRepository.delete(_id);
        }
    }

}
