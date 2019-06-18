package com.chinatelecom.xjdh.bean;

import java.util.List;

public class CityBean {

    private int ret;
    private List<Data> data;


    public class Data{
        private String city_code;
        private String name;

        public String getCitycode() {
            return city_code;
        }

        public void setCitycode(String citycode) {
            this.city_code = citycode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
