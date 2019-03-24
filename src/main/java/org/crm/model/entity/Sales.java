package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sales")
public class Sales implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "sales_channel", length = 50)
    private String salesChannel;
    @Column(name = "sales_date", nullable = false)
    private Date salesDate;
    @Column(name = "customer_id", length = 40, nullable = false)
    private String customerId;
    @Column(name = "customer_name", length = 250)
    private String customerName;
    @Column(name = "sales_oil", length = 250)
    private String salesOil;
    @Column(name = "manager_id", length = 40)
    private String managerId;
    @Column(name = "manager_name", length = 250)
    private String managerName;
    @Column(name = "sales_station", length = 250)
    private String salesStation;
    @Column(name = "sales_count")
    private Double salesCount;
    @Column(name = "sales_price")
    private Double salesPrice;
    @Column(name = "is_transfer", length = 10)
    private String transfer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSalesOil() {
        return salesOil;
    }

    public void setSalesOil(String salesOil) {
        this.salesOil = salesOil;
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

    public String getSalesStation() {
        return salesStation;
    }

    public void setSalesStation(String salesStation) {
        this.salesStation = salesStation;
    }

    public Double getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Double salesCount) {
        this.salesCount = salesCount;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }
}
