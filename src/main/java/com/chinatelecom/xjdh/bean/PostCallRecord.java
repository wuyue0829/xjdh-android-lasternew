package com.chinatelecom.xjdh.bean;

public class PostCallRecord {
    public String caller_number;
    public String called_number;
    public String call_type;
    public String call_time;
    public String access_token;


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

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
