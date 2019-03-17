package org.crm.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "s_menu")
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_no", length = 10, unique = true, nullable = false)
    private String menuNo;
    @Column(name = "menu_name", length = 100, nullable = false)
    private String menuName;
    @Column(name = "action_url", length = 500)
    private String actionUrl;
    @Column(name = "parent_no", length = 10)
    private String parentNo;
    @Column(name = "icon", length = 500)
    private String icon;
    @Column(name = "sort_no", length = 11, columnDefinition = "int default 99")
    private Integer sortNo;

    @Transient
    private List<Menu> children = new ArrayList<>(0);

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
