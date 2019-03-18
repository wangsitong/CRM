package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "manager")
public class Manager implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "manager_id", length = 40, unique = true, nullable = false)
    private String managerId;
    @Column(name = "manager_name", length = 250, unique = true, nullable = false)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
