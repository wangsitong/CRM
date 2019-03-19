package org.crm.common;

public class PageInfo {

    private int currentPage = 1;
    private int pageSize = 10;
    private int total;
    private int totalPages;


    public PageInfo(int currentPage, int pageSize, int total) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public int getTotalPages() {
        if (this.total % this.pageSize == 0) {
            this.totalPages = this.total / this.pageSize;
        } else {
            this.totalPages = this.total / this.pageSize + 1;
        }
        return this.totalPages;
    }

}
