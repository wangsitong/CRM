package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "customer_id", length = 40, unique = true, nullable = false)
    private String code;
    @Column(name = "customer_name", length = 250, nullable = false)
    private String name;
    @Column(name = "customer_address", length = 250)
    private String address;
    @Column(name = "customer_area", length = 50)
    private String area;
    @Column(name = "manager_id", length = 40)
    private String managerId;
    @Column(name = "customer_manager", length = 250)
    private String managerName;
    @Column(name = "customer_sales_channel", length = 10)
    private String salesChannel;
    @Column(name = "customer_level", length = 10)
    private String level;
    @Column(name = "customer_dep", length = 10)
    private String dep;

    @Column(name = "analysis_exclude", length = 1)
    private String analysisExclude;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getAnalysisExclude() {
        return analysisExclude;
    }

    public void setAnalysisExclude(String analysisExclude) {
        this.analysisExclude = analysisExclude;
    }
}
