package org.crm.model.repository;

import org.crm.model.entity.ManagerTask;

import java.util.List;

public interface ManagerTaskRepository {

    List<ManagerTask> find(ManagerTask condition, int firstResult, int maxResults);

    int findCount(ManagerTask condition);

    void insert(ManagerTask instance);

    void update(ManagerTask instance);

    void delete(String id);

    void deleteByManagerId(String managerId);

}
