package org.crm.model.dto;

import org.crm.model.entity.Sales;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class SalesDTO extends Sales implements Serializable {

    private String salesStationNotEquals;
    private String salesChannelNotEquals;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startSalesDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSalesDate;

    private String salesArea;

    public String getSalesStationNotEquals() {
        return salesStationNotEquals;
    }

    public void setSalesStationNotEquals(String salesStationNotEquals) {
        this.salesStationNotEquals = salesStationNotEquals;
    }

    public String getSalesChannelNotEquals() {
        return salesChannelNotEquals;
    }

    public void setSalesChannelNotEquals(String salesChannelNotEquals) {
        this.salesChannelNotEquals = salesChannelNotEquals;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public Date getStartSalesDate() {
        return startSalesDate;
    }

    public void setStartSalesDate(Date startSalesDate) {
        this.startSalesDate = startSalesDate;
    }

    public Date getEndSalesDate() {
        return endSalesDate;
    }

    public void setEndSalesDate(Date endSalesDate) {
        this.endSalesDate = endSalesDate;
    }
}
