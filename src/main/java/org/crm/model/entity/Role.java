package org.crm.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "s_role")
public class Role implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
