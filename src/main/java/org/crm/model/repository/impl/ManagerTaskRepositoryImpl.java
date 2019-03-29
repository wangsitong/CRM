package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.ManagerTask;
import org.crm.model.repository.ManagerTaskRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ManagerTaskRepositoryImpl implements ManagerTaskRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ManagerTask> find(ManagerTask condition, int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from manager_task where 1=1 ");
        Map<String, Object> params = this.getQueryParams(sql, condition);
        Query query = this.entityManager.createNativeQuery(sql.toString(), ManagerTask.class);
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(ManagerTask condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from manager_task where 1=1 ");
        Map<String, Object> params = this.getQueryParams(sql, condition);
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> getQueryParams(StringBuilder sql, ManagerTask condition) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getManagerId())) {
                sql.append("and manager_id = :managerId ");
                params.put("managerId", condition.getManagerId());
            }
            if (condition.getDate() != null) {
                sql.append("and date_format(date, '%Y%m') = :date ");
                params.put("date", (new SimpleDateFormat("yyyyMM").format(condition.getDate())));
            }
        }
        return params;
    }

    @Override
    public void insert(ManagerTask instance) {
        this.entityManager.persist(instance);
    }

    @Override
    public void update(ManagerTask instance) {
        this.entityManager.merge(instance);
    }

    @Override
    public void delete(String id) {
        this.entityManager.remove(this.entityManager.find(ManagerTask.class, id));
    }

    @Override
    public void deleteByManagerId(String managerId) {
        String hql = "delete from ManagerTask where managerId = :managerId";
        Query query = this.entityManager.createQuery(hql);
        query.setParameter("managerId", managerId);
        query.executeUpdate();
    }
}
