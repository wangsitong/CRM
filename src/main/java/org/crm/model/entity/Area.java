package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "area")
public class Area implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "area_id", length = 40, unique = true, nullable = false)
    private String code;
    @Column(name = "area_name", length = 250, unique = true, nullable = false)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
