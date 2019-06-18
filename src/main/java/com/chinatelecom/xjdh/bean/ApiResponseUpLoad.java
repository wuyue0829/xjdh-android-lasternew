package com.chinatelecom.xjdh.bean;

@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseUpLoad {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("data")
	private String data;
	@org.codehaus.jackson.annotate.JsonProperty("newGrouping")
	private String newGrouping;

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNewGrouping() {
		return newGrouping;
	}

	public void setNewGrouping(String newGrouping) {
		this.newGrouping = newGrouping;
	}
	
}
