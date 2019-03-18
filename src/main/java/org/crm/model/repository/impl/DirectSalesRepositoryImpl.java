package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.DirectSales;
import org.crm.model.entity.Sales;
import org.crm.model.repository.DirectSalesRepository;
import org.crm.model.repository.SalesRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DirectSalesRepositoryImpl implements DirectSalesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Sales findById(String id) {
        return this.entityManager.find(Sales.class, id);
    }

    @Override
    public List<?> findByCondition(DirectSales condition, int fistResult, int maxResults) {
        StringBuilder hql = new StringBuilder("select s from DirectSales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createQuery(hql.toString());
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        query.setFirstResult(fistResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(DirectSales condition) {
        StringBuilder hql = new StringBuilder("select count(s) from DirectSales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createQuery(hql.toString());
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(DirectSales condition, StringBuilder hql) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                hql.append("and customerId = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                hql.append("and salesChannel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
        }
        return params;
    }

    @Override
    public void save(DirectSales sales) {
        this.entityManager.persist(sales);
    }

    @Override
    public void delete(String id) {
        DirectSales instance = this.entityManager.find(DirectSales.class, id);
        this.entityManager.remove(instance);
    }
}
