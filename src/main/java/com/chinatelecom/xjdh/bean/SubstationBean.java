package com.chinatelecom.xjdh.bean;


import java.util.List;

public class SubstationBean {
    private int ret;
    private List<Data> data;


    public class Data{
        private String id;
        private String name;
        private String city;
        private String city_code;
        private String Stationcode;

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getStationcode() {
            return Stationcode;
        }

        public void setStationcode(String stationcode) {
            Stationcode = stationcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
