package com.taotao.common.pojo;

/**
 * Created by hu on 2018-05-25.
 */
public class TreeNode {

    private long id;
    private String text;
    private String state;

    public TreeNode(long id, String text, String state){
        this.id = id;
        this.text = text;
        this.state = state;
    }

    public TreeNode(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
