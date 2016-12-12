package com.example.closemanagement_trainservice.bean;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 潘洲涛 on 2016/12/4.
 */

public class ListBean implements Serializable{
    private String dept_name;   //整改部门
    private String remark;      //问题
    private String find_user;   //发现人
    private String zr_user;       //整改人
    private String rectification;   //整改情况
    private String status;      //整改状态

    public ListBean() {
        super();
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFind_user() {
        return find_user;
    }

    public void setFind_user(String find_user) {
        this.find_user = find_user;
    }

    public String getZr_user() {
        return zr_user;
    }

    public void setZr_user(String zr_user) {
        this.zr_user = zr_user;
    }

    public String getRectification() {
        return rectification;
    }

    public void setRectification(String rectification) {
        this.rectification = rectification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ListBean{" +
                "dept_name='" + dept_name + '\'' +
                ", remark='" + remark + '\'' +
                ", find_user='" + find_user + '\'' +
                ", zr_user='" + zr_user + '\'' +
                ", rectification='" + rectification + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
