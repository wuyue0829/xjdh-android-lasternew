package com.chinatelecom.xjdh.bean;

public class CheckCityList {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private int city_code;
	@org.codehaus.jackson.annotate.JsonProperty("name")
	private String city_name;
	public int getCity_code() {
		return city_code;
	}
	public void setCity_code(int city_code) {
		this.city_code = city_code;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	@Override
	public String toString() {
		return "City [city_code=" + city_code + ", city_name=" + city_name + "]";
	}
	
}
