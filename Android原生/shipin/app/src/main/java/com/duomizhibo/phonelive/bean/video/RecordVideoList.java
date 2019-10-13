package com.duomizhibo.phonelive.bean.video;


import java.util.List;

public class RecordVideoList {

    private int total;
    private int page;
    private int pagesize;
    private int totalPage;
    private List<RecordVideo> rows;

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

    public List<RecordVideo> getRows() {
        return rows;
    }

    public void setRows(List<RecordVideo> rows) {
        this.rows = rows;
    }


}
