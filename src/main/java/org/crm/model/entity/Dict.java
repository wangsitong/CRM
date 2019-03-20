package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "s_dict")
public class Dict implements Serializable {

    @Id
    @Column(name = "key", length = 10, unique = true, nullable = false)
    private String key;
    @Column(name = "value", length = 250, nullable = false)
    private String value;
    @Column(name = "parent", length = 10, nullable = false)
    private String parent;
    @Column(name = "sort_no", length = 11, nullable = false, columnDefinition = "int default 99")
    private int sortNo = 99;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

}
