package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.SalesPlan;
import org.crm.model.repository.SalesPlanRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SalesPlanRepositoryImpl implements SalesPlanRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SalesPlan> find(SalesPlan condition, int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from sales_plan where 1=1 ");
        Map<String, Object> params = this.getQueryParams(sql, condition);
        sql.append("order by date desc");
        Query query = this.entityManager.createNativeQuery(sql.toString(), SalesPlan.class);
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(SalesPlan condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from sales_plan where 1=1 ");
        Map<String, Object> params = this.getQueryParams(sql, condition);
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> getQueryParams(StringBuilder sql, SalesPlan condition) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getExecutorId())) {
                sql.append("and executor_id = :executorId ");
                params.put("executorId", condition.getExecutorId());
            }
            if (condition.getDate() != null) {
                sql.append("and date_format(date, '%Y%m') = :date ");
                params.put("date", (new SimpleDateFormat("yyyyMM").format(condition.getDate())));
            }
            if (StringUtils.isNotBlank(condition.getType())) {
                sql.append("and type = :type ");
                params.put("type", condition.getType());
            }
        }
        return params;
    }

    @Override
    public void insert(SalesPlan instance) {
        this.entityManager.persist(instance);
    }

    @Override
    public void update(SalesPlan instance) {
        this.entityManager.merge(instance);
    }

    @Override
    public void delete(String id) {
        this.entityManager.remove(this.entityManager.find(SalesPlan.class, id));
    }

    @Override
    public void deleteByExecutorId(String executorId) {
        String hql = "delete from SalesPlan where executorId = :executorId";
        Query query = this.entityManager.createQuery(hql);
        query.setParameter("executorId", executorId);
        query.executeUpdate();
    }
}
