package org.crm.model.repository;

import java.util.List;

public interface CustomerRepository<T> {

    <T> T findById(String id);

    <T> T findByCode(String code);

    List<T> findList(T condition, int firstResult, int maxResults);

    int findCount(T condition);

    void insert(T instance);

    void update(T instance);

    void delete(String id);

}
