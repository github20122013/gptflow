package com.concentrate.gpt.gptflow.service.tree;

import java.util.Collection;

public interface Node {
    public String getId();

    public void setId(String id) ;

    public String getParentId() ;

    public void setParentId(String parentId) ;

    public int getLevel() ;

    public void setLevel(int level);

    public Collection<Node> getChildren();

    public void setChildren(Collection<Node> children);

    public Node getParent() ;

    public void setParent(Node parent) ;

    public boolean isLeaf();

    public void setLeaf(boolean leaf) ;

    public void setStat(int stat);
    int getCoreRoot();

    void setCoreRoot(int coreRoot);

    public int getDocId() ;

    public void setDocId(int docId);
    public String getStatus();

    public void setStatus(String status) ;

    public String getMsg();

    public void setMsg(String msg) ;
}
