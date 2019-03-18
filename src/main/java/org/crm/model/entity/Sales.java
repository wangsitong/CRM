package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "all_channel_sales")
public class Sales implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "sales_date", nullable = false)
    private Date salesDate;
    @Column(name = "customer_id", length = 40, nullable = false)
    private String customerId;
    @Column(name = "customer_name", length = 250)
    private String customerName;
    @Column(name = "sales_oil", length = 250)
    private String salesOil;
    @Column(name = "customer_manager_id", length = 40)
    private String customerManagerId;
    @Column(name = "customer_manager", length = 250)
    private String customerManager;
    @Column(name = "sales_station", length = 250)
    private String salesStation;
    @Column(name = "sales_count")
    private Double salesCount;
    @Column(name = "sales_price")
    private Double salesPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCustomerManagerId() {
        return customerManagerId;
    }

    public void setCustomerManagerId(String customerManagerId) {
        this.customerManagerId = customerManagerId;
    }

    public String getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(String customerManager) {
        this.customerManager = customerManager;
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
}
