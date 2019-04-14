package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @Column(name = "id", length = 40, nullable = false, unique = true)
    private String id;
    @Column(name = "station_id", length = 50)
    private String stationId;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "address", length = 250)
    private String address;
    @Column(name = "area", length = 50)
    private String area;
    @Column(name = "property", length = 50)
    private String property;
    @Column(name = "transfer", length = 1)
    private String transfer;

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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }
}
