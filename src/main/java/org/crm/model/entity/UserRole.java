package org.crm.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "s_user_role")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "user_id", length = 40, nullable = false)
    private String userId;
    @Column(name = "role_id", length = 40, nullable = false)
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
