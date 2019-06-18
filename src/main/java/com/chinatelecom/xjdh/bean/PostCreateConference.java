package com.chinatelecom.xjdh.bean;

public class PostCreateConference {
    public String access_token;
    private String caller_number;
    private String called_number;

    public String getCaller_number() {
        return caller_number;
    }

    public void setCaller_number(String caller_number) {
        this.caller_number = caller_number;
    }

    public String getCalled_number() {
        return called_number;
    }

    public void setCalled_number(String called_number) {
        this.called_number = called_number;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
