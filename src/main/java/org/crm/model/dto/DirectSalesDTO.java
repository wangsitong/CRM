package org.crm.model.dto;

import org.crm.model.entity.DirectSales;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class DirectSalesDTO extends DirectSales implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startSalesDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSalesDate;

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
