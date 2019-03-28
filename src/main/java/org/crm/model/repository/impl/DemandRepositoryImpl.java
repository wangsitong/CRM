package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.Demand;
import org.crm.model.repository.DemandRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DemandRepositoryImpl implements DemandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Demand> findAll(Demand condition, int firstResult, int maxResults) {
        StringBuilder hql = new StringBuilder();
        hql.append("select d from Demand d where 1=1 ");

        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(Demand condition) {
        StringBuilder hql = new StringBuilder();
        hql.append("select count(d.id) from Demand d where 1=1 ");
        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    public Map<String, Object> setQueryParams(StringBuilder hql, Demand condition) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                hql.append("and customerId = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getYear())) {
                hql.append("and year = :year ");
                params.put("year", condition.getYear());
            }
        }
        return params;
    }

    @Override
    public List<Map<String, Object>> getAreas(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.customer_area as area,");
        sql.append("sum(d.customer_demand_gas) as plan_gas, sum(d.customer_demand_diesel) as plan_diesel ");
        sql.append("from customer c left join customer_demand d on c.customer_id = d.customer_id ");
        sql.append("where 1=1 ");
        sql.append("and c.customer_id in (select customer_id from private_station) ");
        sql.append("and d.customer_demand_year = :year ");
        sql.append("group by c.customer_area ");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("year", year);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public Map<String, Object> findTotal(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(customer_demand_gas) gas,");
        sql.append("sum(customer_demand_diesel) diesel ");
        sql.append("from customer_demand ");
        sql.append("where customer_demand_year = :year");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("year", year);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> dataList = query.getResultList();
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }

    @Override
    public void save(Demand instance) {
        this.entityManager.persist(instance);
    }

    @Override
    public void update(Demand instance) {
        this.entityManager.merge(instance);
    }

    @Override
    public void delete(String id) {
        this.entityManager.remove(this.entityManager.find(Demand.class, id));
    }

    @Override
    public void deleteByCustomer(String customerId) {
        String hql = "delete from Demand where customerId = :customerId";
        Query query = this.entityManager.createQuery(hql, Demand.class);
        query.setParameter("customerId", customerId);
        query.executeUpdate();
    }

}
