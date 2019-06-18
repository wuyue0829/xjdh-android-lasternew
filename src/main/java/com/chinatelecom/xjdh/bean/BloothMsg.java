package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class BloothMsg {
	@org.codehaus.jackson.annotate.JsonProperty("msg")
	private String msg[];

	public String[] getMsg() {
		return msg;
	}

	public void setMsg(String[] msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BloothMsg [msg=" + Arrays.toString(msg) + "]";
	}
	
	
}
