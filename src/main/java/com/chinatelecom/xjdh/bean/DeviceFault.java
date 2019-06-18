package com.chinatelecom.xjdh.bean;

public class DeviceFault {
	@org.codehaus.jackson.annotate.JsonProperty("data_id")
	private String data_id;
	@org.codehaus.jackson.annotate.JsonProperty("name")
    private String name;
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "DeviceFault [data_id=" + data_id + ", name=" + name + "]";
	}
	
	
}
