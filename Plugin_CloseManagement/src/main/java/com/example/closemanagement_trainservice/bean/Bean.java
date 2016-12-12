package com.example.closemanagement_trainservice.bean;

import java.io.Serializable;

/**
 * Created by 潘洲涛 on 2016/12/5.
 */

public class Bean implements Serializable{
    private int pageNumber;
    private int pageSize;
    private Boolean firstPage;
    private Boolean lastPage;
    private String id;
    private String status;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        this.firstPage = firstPage;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public Bean() {
        super();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "pageNumber='" + pageNumber + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
