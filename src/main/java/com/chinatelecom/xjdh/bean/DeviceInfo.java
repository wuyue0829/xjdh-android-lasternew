package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class DeviceInfo {

	@org.codehaus.jackson.annotate.JsonProperty("data_id")
	private String data_id;
	@org.codehaus.jackson.annotate.JsonProperty("name")
    private String name;
	@org.codehaus.jackson.annotate.JsonProperty("content")
    private String[] content;
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
	public String[] getContent() {
		return content;
	}
	public void setContent(String[] content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "DeviceInfo [data_id=" + data_id + ", name=" + name + ", content=" + Arrays.toString(content) + "]";
	}
	
	
}
