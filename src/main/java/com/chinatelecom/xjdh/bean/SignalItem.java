package com.chinatelecom.xjdh.bean;

public class SignalItem {
	@org.codehaus.jackson.annotate.JsonProperty("key")
	private String key;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@org.codehaus.jackson.annotate.JsonProperty("val")
	private String val;

	public void setVal(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

}
