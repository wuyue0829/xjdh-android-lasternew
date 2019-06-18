package com.chinatelecom.xjdh.bean;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResponse {
	
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private int ret;
	@org.codehaus.jackson.annotate.JsonProperty("msg")
	private String msg;
	@Override
	public String toString() {
		return "JsonResponse [ret=" + ret + ", msg=" + msg + "]";
	}
	
}
