package org.crm.model.repository;

import org.crm.model.entity.Role;
import org.crm.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    @Query("select r from UserRole ur left join Role r on ur.roleId = r.id where ur.userId = ?1")
    List<Role> findByUserId(String userId);

    UserRole findByUserIdAndRoleId(String userId, String roleId);

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

}
