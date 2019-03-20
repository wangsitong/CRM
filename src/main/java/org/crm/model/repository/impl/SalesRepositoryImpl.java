package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.Sales;
import org.crm.model.repository.SalesRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SalesRepositoryImpl implements SalesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Sales findById(String id) {
        return this.entityManager.find(Sales.class, id);
    }

    @Override
    public List<?> findByCondition(Sales condition, int fistResult, int maxResults) {
        StringBuilder hql = new StringBuilder("select s from Sales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        query.setFirstResult(fistResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(Sales condition) {
        StringBuilder hql = new StringBuilder("select count(s) from Sales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(Sales condition, StringBuilder hql) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                hql.append("and customerId = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                hql.append("and station like :station ");
                params.put("station", "%" + condition.getSalesStation() + "%");
            }
        }
        return params;
    }

    @Override
    public void save(Sales sales) {
        this.entityManager.persist(sales);
    }

    @Override
    public void delete(String id) {
        Sales instance = this.entityManager.find(Sales.class, id);
        this.entityManager.remove(instance);
    }
}