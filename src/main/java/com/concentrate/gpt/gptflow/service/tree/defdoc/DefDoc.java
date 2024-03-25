package com.concentrate.gpt.gptflow.service.tree.defdoc;

import com.concentrate.gpt.gptflow.service.tree.DefaultNode;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

public class DefDoc extends DefaultNode {
    private String docCode;
    private String docName;
    private String name;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private String updateUser;
    private Map<String, Object> detail = Maps.newHashMap();

    public String getDocCode() {
        return docCode;
    }
    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }
    public String getDocName() {
        return docName;
    }
    public String getName() {
        return docName;
    }
    public void setDocName(String docName) {
        this.docName = docName;
        this.name = docName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Map<String, Object> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }

}
