package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
import org.crm.model.entity.Role;
import org.crm.model.entity.User;
import org.crm.model.entity.UserRole;
import org.crm.model.repository.RoleObjectRepository;
import org.crm.model.repository.UserRepository;
import org.crm.model.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleObjectRepository roleObjectRepository;

    public User getByUserName(String userName) {
        List<User> dataList = this.userRepository.findByProperty("userName", userName);
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }

    public User getByUserId(String userId) {
        List<User> dataList = this.userRepository.findByProperty("userId", userId);
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }

    public User getById(String id) {
        return this.userRepository.findById(id);
    }

    public PageDTO<User> getList(User condition, int page, int pageSize) {
        int total = this.userRepository.findCount(condition);
        List<User> dataList = this.userRepository.findList(condition, (page - 1) * pageSize, pageSize);

        return new PageDTO<>(total, dataList);
    }

    @Transactional
    public void save(User user) throws Exception {
        User u = this.getByUserId(user.getUserId());
        if (StringUtils.isBlank(user.getId())) {
            if (u != null) {
                throw new Exception("User('" + user.getUserId() + "') exists");
            }
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.userRepository.insert(user);
        } else {
            u.setUserName(user.getUserName());
            this.userRepository.update(u);
        }
    }

    @Transactional
    public void delete(String[] ids) {
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            this.userRepository.delete(id);
        }
    }

    public List<?> getRoles(String userId) {
        List<Role> roles = this.userRoleRepository.findByUserId(userId);
        for (Role role : roles) {
            role.setObjects(this.roleObjectRepository.findObjectsByRoleId(role.getId()));
        }
        return roles;
    }

    @Transactional
    public void addRoles(String userId, String[] roleIds) {
        if (StringUtils.isBlank(userId)) {
            return;
        }
        this.userRoleRepository.deleteByUserId(userId);
        for (String roleId : roleIds) {
            if (StringUtils.isBlank(roleId)) {
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);

            this.userRoleRepository.save(userRole);
        }
    }

}
