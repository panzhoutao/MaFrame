package com.example.closemanagement_trainservice.bean;

import com.zhy.tree.bean.TreeNodeCode;
import com.zhy.tree.bean.TreeNodeId;
import com.zhy.tree.bean.TreeNodeLabel;
import com.zhy.tree.bean.TreeNodePid;

import java.io.Serializable;

/**
 * Created by 潘洲涛 on 2016/12/5.
 */

public class DepartBean implements Serializable{

    /**
     * pym : sytlj
     * leader_user_id : null
     * pid : 0
     * property : null
     * code : 0001
     * available : 2
     * type : null
     * station_level : null
     * deleted : 0
     * url : null
     * id : 1
     * level : 1
     * response_user_id : null
     * name : 沈阳铁路局
     * unit_type : null
     * my_level : 1
     * safe_overtime : null
     * equdepartment : 0
     */

    @TreeNodePid
    private int pid;
    @TreeNodeCode
    private String code;
    @TreeNodeId
    private int id;
    private int level;
    @TreeNodeLabel
    private String name;



    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
