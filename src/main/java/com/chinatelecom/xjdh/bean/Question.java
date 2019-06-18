package com.chinatelecom.xjdh.bean;

public class Question {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("content")
    private String content;
	@org.codehaus.jackson.annotate.JsonProperty("order")
    private int order;
	@org.codehaus.jackson.annotate.JsonProperty("type")
    private String type;
	@org.codehaus.jackson.annotate.JsonProperty("desc")
    private String desc;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String toString() {
		return "Question [id=" + id + ", content=" + content + ", order=" + order + ", type=" + type + ", desc=" + desc
				+ "]";
	}
	
	
}
