package com.chinatelecom.xjdh.bean;


import java.util.List;

public class RoomBean {
    private int ret;
    private List<Data> data;


    public class Data{
        private String id;
        private String name;
        private String substation_id;
        private String phone_number;
        private String city_code;

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubstation_id() {
            return substation_id;
        }

        public void setSubstation_id(String substation_id) {
            this.substation_id = substation_id;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
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
