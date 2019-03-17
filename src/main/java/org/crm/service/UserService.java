package org.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.PageDTO;
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
        return this.userRepository.findByUserName(userName);
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
        if (StringUtils.isBlank(user.getId())) {
            User u = this.userRepository.findByUserName(user.getUserName());
            if (u != null) {
                throw new Exception("User('" + user.getUserName() + "') exists");
            }
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.userRepository.insert(user);
        } else {
            this.userRepository.update(user);
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
        List<UserRole> roles = this.userRoleRepository.findByUserId(userId);
        return roles;
    }

    public List<?> getRoleObjects(String userId) {
        List<UserRole> roles = this.userRoleRepository.findByUserId(userId);
        List<?> roleObjects = new ArrayList<>();
        for (UserRole userRole : roles) {
            List<?> objects = this.roleObjectRepository.findObjectsByRoleId(userRole.getRoleId());
            roleObjects.addAll(new ArrayList(objects));
        }
        return roleObjects;
    }

}
