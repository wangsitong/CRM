package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "private_station")
public class Station implements Serializable {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    private String id;
    @Column(name = "customer_id", length = 50)
    private String stationId;
    @Column(name = "customer_name", length = 50)
    private String name;
    @Column(name = "customer_area", length = 50)
    private String area;
    @Column(name = "competition_station", length = 50)
    private String competitionStation;
    @Column(name = "customer_manager", length = 50)
    private String managerId;
    @Column(name = "manager_name", length = 250)
    private String managerName;
    @Column(name = "memo", length = 50)
    private String memo;
    @Column(name = "customer_address", length = 50)
    private String address;
    @Column(name = "customer_atten", length = 50)
    private String atten;
    @Column(name = "tel", length = 50)
    private String tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompetitionStation() {
        return competitionStation;
    }

    public void setCompetitionStation(String competitionStation) {
        this.competitionStation = competitionStation;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAtten() {
        return atten;
    }

    public void setAtten(String atten) {
        this.atten = atten;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
