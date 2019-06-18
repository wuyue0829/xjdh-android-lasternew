package com.chinatelecom.xjdh.bean;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResp {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("url")
	private String data;

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

	@Override
	public String toString() {
		return "ApiResp [ret=" + ret + ", data=" + data + "]";
	}
	
}
