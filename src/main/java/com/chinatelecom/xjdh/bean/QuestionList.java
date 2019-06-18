package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class QuestionList {
//	@org.codehaus.jackson.annotate.JsonProperty("ret")
	private Integer ret;

//	@org.codehaus.jackson.annotate.JsonProperty("data")
	private String[] data;

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "QuestionList [ret=" + ret + ", data=" + Arrays.toString(data) + "]";
	}
	
}
