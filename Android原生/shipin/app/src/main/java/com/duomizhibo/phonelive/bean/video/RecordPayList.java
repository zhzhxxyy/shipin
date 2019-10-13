package com.duomizhibo.phonelive.bean.video;


import java.util.List;

public class RecordPayList {

    private int total;
    private int page;
    private int pagesize;
    private int totalPage;
    private List<RecordPay> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<RecordPay> getRows() {
        return rows;
    }

    public void setRows(List<RecordPay> rows) {
        this.rows = rows;
    }


    @Override
    public String toString() {
        return "RecordPayList{" +
                "total=" + total +
                ", page=" + page +
                ", pagesize=" + pagesize +
                ", totalPage=" + totalPage +
                ", rows=" + rows +
                '}';
    }
}
