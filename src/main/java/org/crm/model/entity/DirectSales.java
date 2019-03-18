package org.crm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "direct_sales")
public class DirectSales implements Serializable {

    @Id
    @Column(name = "id", length = 40, unique = true, nullable = false)
    private String id;
    @Column(name = "sales_date", nullable = false)
    private Date salesDate;
    @Column(name = "sales_channel", length = 50)
    private String salesChannel;
    @Column(name = "sales_oil", length = 50)
    private String salesOil;
    @Column(name = "customer_id", length = 50)
    private String customerId;
    @Column(name = "customer_name", length = 250)
    private String customerName;
    @Column(name = "sales_satistics", length = 250)
    private String salesSatistics;
    @Column(name = "customer_level", length = 50)
    private String customerLevel;
    @Column(name = "customer_manager", length = 50)
    private String customerManager;

    @Column(name = "sales_count")
    private double salesCount;
    @Column(name = "sales_total")
    private double salesTotal;
    @Column(name = "sales_price")
    private double salesPrice;
    @Column(name = "sales_price_set")
    private double salesPriceSet;
    @Column(name = "sales_price_balance")
    private double salesPriceBalance;

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

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getSalesOil() {
        return salesOil;
    }

    public void setSalesOil(String salesOil) {
        this.salesOil = salesOil;
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

    public String getSalesSatistics() {
        return salesSatistics;
    }

    public void setSalesSatistics(String salesSatistics) {
        this.salesSatistics = salesSatistics;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(String customerManager) {
        this.customerManager = customerManager;
    }

    public double getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(double salesCount) {
        this.salesCount = salesCount;
    }

    public double getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(double salesTotal) {
        this.salesTotal = salesTotal;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getSalesPriceSet() {
        return salesPriceSet;
    }

    public void setSalesPriceSet(double salesPriceSet) {
        this.salesPriceSet = salesPriceSet;
    }

    public double getSalesPriceBalance() {
        return salesPriceBalance;
    }

    public void setSalesPriceBalance(double salesPriceBalance) {
        this.salesPriceBalance = salesPriceBalance;
    }
}
