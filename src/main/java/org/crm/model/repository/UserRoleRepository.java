package org.crm.model.repository;

import org.crm.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserId(String userId);

    UserRole findByUserIdAndRoleId(String userId, String roleId);

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

}
