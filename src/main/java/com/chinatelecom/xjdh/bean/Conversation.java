package com.chinatelecom.xjdh.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


@Table(name = "Conversation")
public class Conversation{
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "username")
    public String username;
    @Column(name = "full_name")
    public String full_name;
    @Column(name = "phone_number")
    public String phone_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

}
