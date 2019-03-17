package org.crm.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "s_role_object")
public class RoleObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "role_id", length = 40, nullable = false)
    private String roleId;
    @Column(name = "object_id", length = 40, nullable = false)
    private String objectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
