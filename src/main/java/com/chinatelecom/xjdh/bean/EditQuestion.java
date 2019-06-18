package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class EditQuestion {
	@org.codehaus.jackson.annotate.JsonProperty("question_id")
	private int question_id;
	@org.codehaus.jackson.annotate.JsonProperty("question")
	private String question;
	@org.codehaus.jackson.annotate.JsonProperty("content")
	private String[] content;
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String[] getContent() {
		return content;
	}
	public void setContent(String[] content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "EditQuestion [question_id=" + question_id + ", question=" + question + ", content="
				+ Arrays.toString(content) + "]";
	}
	
	

}
