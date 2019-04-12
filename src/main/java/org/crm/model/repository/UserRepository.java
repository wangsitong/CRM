package org.crm.model.repository;

import org.crm.model.entity.User;

import java.util.List;

public interface UserRepository {

    User findById(String id);

    List<User> findByProperty(String propertyName, Object propertyValue);

    List<User> findList(User condition, int firstResult, int maxResults);

    int findCount(User condition);

    void insert(User user);

    void update(User user);

    void delete(String id);

}
