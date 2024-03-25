package com.concentrate.gpt.gptflow.service.tree;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class DefaultNode implements Node,Serializable {

    public static final Integer INITIAL_ID_SEQUENCE = 0;

    protected int coreRoot;
    protected String id;
    protected String parentId;
    protected int level ;
    @JSONField(serialize = false)
    protected Node root;
    @JSONField(serialize = false)
    protected Node parent;
    protected boolean leaf;
    protected boolean highlight;

    protected int docId;//doc表数据库主键ID
    protected Collection<Node> children = new ArrayList<Node>();
    /**
     * 目录状态， 用于展示，0：正常，1：存在pc不存在终端，2：存在终端不存在pc，3：pc端的虚拟目录（不挂商品）, 4: 无结果分类
     */
    protected int stat = 0;

    /**
     * 频道下目录数据是否发生变化，如果发生变化需要同步，1：变化，2：未变化
     */
    protected int isChanged = 0;

    protected String remark;

    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getCoreRoot() {
        return coreRoot;
    }

    public void setCoreRoot(int coreRoot) {
        this.coreRoot = coreRoot;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return children==null || children.size()==0;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public void setChildren(Collection<Node> children) {
        this.children = children;
    }

    @Override
    public Collection<Node> getChildren() {
        return this.children;
    }

	public int getStat() {
		return stat;
	}

	@Override
	public void setStat(int stat) {
		this.stat = stat;
	}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }
}
