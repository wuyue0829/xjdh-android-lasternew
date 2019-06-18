package com.chinatelecom.xjdh.bean;

import java.util.List;

public class ConversationBean {

    private int ret;
    private List<Conversation> data;


    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<Conversation> getData() {
        return data;
    }

    public void setData(List<Conversation> data) {
        this.data = data;
    }
}
