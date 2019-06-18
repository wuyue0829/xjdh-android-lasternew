package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class Technology {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private int id;
	@org.codehaus.jackson.annotate.JsonProperty("content")
    private String content;
	
	@org.codehaus.jackson.annotate.JsonProperty("desc")
    private String desc;
	
	@org.codehaus.jackson.annotate.JsonProperty("answer")
    private String[] answer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String[] getAnswer() {
		return answer;
	}

	public void setAnswer(String[] answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "Technology [id=" + id + ", content=" + content + ", desc=" + desc + ", answer="
				+ Arrays.toString(answer) + "]";
	}
	
	
	

}
