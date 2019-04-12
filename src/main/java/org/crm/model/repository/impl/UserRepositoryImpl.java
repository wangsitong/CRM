package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.entity.User;
import org.crm.model.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findById(String id) {
        return this.entityManager.find(User.class, id);
    }

    @Override
    public List<User> findByProperty(String propertyName, Object propertyValue) {
        String hql = "from User where " + propertyName + " = :property";
        Query query = this.entityManager.createQuery(hql);
        query.setParameter("property", propertyValue);
        List dataList = query.getResultList();
        return dataList;
    }

    @Override
    public List<User> findList(User condition, int firstResult, int maxResults) {
        StringBuilder hql = new StringBuilder("from User where 1=1 ");
        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(User condition) {
        StringBuilder hql = new StringBuilder("select count(id) from User where 1=1 ");
        Map<String, Object> params = this.setQueryParams(hql, condition);
        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(StringBuilder hql, User condition) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getUserName())) {
                hql.append("and userName like :userName ");
                params.put("userName", "%" + condition.getUserName() + "%");
            }
            if (StringUtils.isNotBlank(condition.getType())) {
                hql.append("and type = :type ");
                params.put("type", condition.getType());
            }
        }

        return params;
    }

    @Override
    public void insert(User user) {
        this.entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        this.entityManager.merge(user);
    }

    @Override
    public void delete(String id) {
        User user = this.entityManager.find(User.class, id);
        this.entityManager.remove(user);
    }
}
