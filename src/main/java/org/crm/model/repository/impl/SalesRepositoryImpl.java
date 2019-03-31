package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.dto.SalesDTO;
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
    public List<?> findByCondition(SalesDTO condition, int fistResult, int maxResults) {
        StringBuilder hql = new StringBuilder("select s from Sales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        hql.append("order by s.salesDate desc");
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        query.setFirstResult(fistResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(SalesDTO condition) {
        StringBuilder hql = new StringBuilder("select count(s) from Sales s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(SalesDTO condition, StringBuilder hql) {
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
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                hql.append("and salesChannel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
            if (condition.getStartSalesDate() != null) {
                hql.append("and salesDate >= :startDate ");
                params.put("startDate", condition.getStartSalesDate());
            }
            if (condition.getEndSalesDate() != null) {
                hql.append("and salesDate <= :endDate ");
                params.put("endDate", condition.getEndSalesDate());
            }
            if (StringUtils.isNotBlank(condition.getSalesStationNotEquals())) {
                hql.append("and salesStation <> :stationNotEquals ");
                params.put("stationNotEquals", condition.getSalesStationNotEquals());
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
