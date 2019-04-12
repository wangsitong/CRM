package org.crm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@JsonIgnoreProperties(value = { "password" })
public class User implements Serializable {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    private String id;
    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;
    @Column(name = "user_name", length = 20, nullable = false, unique = true)
    private String userName;
    @Column(name = "password", length = 40, nullable = false)
    @Transient
    private String password;
    @Column(name = "user_type", length = 10, nullable = false)
    private String type;

    @Transient
    private List<?> roles = new ArrayList<>();

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<?> getRoles() {
        return roles;
    }

    public void setRoles(List<?> roles) {
        this.roles = roles;
    }
}
