package org.crm.common;

import java.util.List;

public class PageDTO<E> {

    private List<E> dataList;

    private int total;

    public PageDTO(int total, List<E> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
