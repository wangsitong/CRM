package org.crm.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "s_role")
public class Role implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Transient
    private List<?> objects = new ArrayList<>();

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

    public List<?> getObjects() {
        return objects;
    }

    public void setObjects(List<?> objects) {
        this.objects = objects;
    }
}
