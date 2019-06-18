package com.chinatelecom.xjdh.bean;

public class ApiResponseUrl {
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private Integer ret;
	
	@org.codehaus.jackson.annotate.JsonProperty("rtsp_url")
	private String rtsp_url;

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public String getRtsp_url() {
		return rtsp_url;
	}

	public void setRtsp_url(String rtsp_url) {
		this.rtsp_url = rtsp_url;
	}

	@Override
	public String toString() {
		return "ApiResponseUrl [ret=" + ret + ", rtsp_url=" + rtsp_url + "]";
	}
	
	
}
