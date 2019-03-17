package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.model.entity.Role;
import org.crm.model.entity.RoleObject;
import org.crm.model.repository.RoleObjectRepository;
import org.crm.model.repository.RoleRepository;
import org.crm.model.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleObjectRepository roleObjectRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<Role> getList() {
        return this.roleRepository.findAll();
    }

    public List<?> getObjects(String id) {
        return this.roleObjectRepository.findObjectsByRoleId(id);
    }

    @Transactional
    public void save(Role role) {
        if (StringUtils.isBlank(role.getId())) {
            role.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        this.roleRepository.save(role);
    }

    @Transactional
    public void saveRoleObjects(String roleId, String[] objectIds) {
        this.roleObjectRepository.deleteByRoleId(roleId);
        for (String objectId : objectIds) {
            if (StringUtils.isBlank(objectId)) {
                continue;
            }
            RoleObject roleObject = new RoleObject();
            roleObject.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            roleObject.setRoleId(roleId);
            roleObject.setObjectId(objectId);

            this.roleObjectRepository.save(roleObject);
        }
    }

    @Transactional
    public void delete(String id) {
        String[] ids = id.split(",");
        for (String roleId : ids) {
            this.roleObjectRepository.deleteByRoleId(roleId);
            this.userRoleRepository.deleteByRoleId(roleId);
            this.roleRepository.deleteById(roleId);
        }
    }

}
