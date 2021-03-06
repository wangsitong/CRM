package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.Customer;
import org.crm.model.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository<Customer> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer findById(String id) {
        return this.entityManager.find(Customer.class, id);
    }

    @Override
    public Customer findByCode(String code) {
        return this.findByProperty("code", code);
    }

    @Override
    public Customer findByName(String name) {
        return this.findByProperty("name", name);
    }

    public Customer findByProperty(String property, String value) {
        StringBuilder hql = new StringBuilder("from Customer c where " + property + " = :" + property + " ");
        Query query = this.entityManager.createQuery(hql.toString());
        query.setParameter(property, value);
        List<?> dataList = query.getResultList();
        if (dataList != null && !dataList.isEmpty()) {
            return (Customer) dataList.get(0);
        }
        return null;
    }

    @Override
    public List findList(Customer condition, int firstResult, int maxResults) {
        StringBuilder hql = new StringBuilder("from Customer c where 1=1 ");
        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(Customer condition) {
        StringBuilder hql = new StringBuilder("select count(c) from Customer c where 1=1 ");
        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number) query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(StringBuilder hql, Customer condition) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getName())) {
                hql.append("and (name like :name) ");
                params.put("name", "%" + condition.getName() + "%");
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                hql.append("and salesChannel = :channel ");
                params.put("channel", condition.getSalesChannel());
            }
        }
        return params;
    }

    @Override
    public void insert(Customer instance) {
        this.entityManager.persist(instance);
    }

    @Override
    public void update(Customer instance) {
        this.entityManager.merge(instance);
    }

    @Override
    public void delete(String id) {
        this.entityManager.remove(this.entityManager.find(Customer.class, id));
    }
}
