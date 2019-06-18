package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseImage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4761457078634243041L;
	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private Integer ret;

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Integer getRet() {
		return ret;
	}

	@org.codehaus.jackson.annotate.JsonProperty("data")
	private StationList [] data;

	public StationList[] getData() {
		return data;
	}

	public void setData(StationList[] data) {
		this.data = data;
	}

	
}
