package com.chinatelecom.xjdh.bean;

public class Country {
	@org.codehaus.jackson.annotate.JsonProperty("substation_id")
	private int country_code;
	@org.codehaus.jackson.annotate.JsonProperty("substation_name")
	private String country_name;
	public int getCountry_code() {
		return country_code;
	}
	public void setCountry_code(int country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	@Override
	public String toString() {
		return "Country [country_code=" + country_code + ", country_name=" + country_name + "]";
	}

}
