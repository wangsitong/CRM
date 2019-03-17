package org.crm.model.repository;

import org.crm.model.entity.RoleObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleObjectRepository extends JpaRepository<RoleObject, String> {

    List<?> findByRoleId(String roleId);

    @Query(value = "select m from Menu m left join RoleObject ro on m.menuNo = ro.objectId where ro.roleId = ?1")
    List<?> findObjectsByRoleId(String roleId);

    void deleteByRoleId(String roleId);

}
