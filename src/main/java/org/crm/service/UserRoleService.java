package org.crm.service;

import org.crm.model.entity.UserRole;
import org.crm.model.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<?> getRoles(String userId) {
        return this.userRoleRepository.findByUserId(userId);
    }

    @Transactional
    public void add(String userId, String roleId) {
        UserRole userRole = this.userRoleRepository.findByUserIdAndRoleId(userId, roleId);
        if (userRole != null) {
            return;
        }
        userRole = new UserRole();
        userRole.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        this.userRoleRepository.save(userRole);
    }

    @Transactional
    public void delete(String id) {
        this.userRoleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        this.userRoleRepository.deleteByUserId(userId);
    }

}
