/* Generated by JavaFromJSON */
/*http://javafromjson.dashingrocket.com*/

package com.chinatelecom.xjdh.bean;
/**
 * @author peter
 * 
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class MessageItem {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("title")
	private String title;
	@org.codehaus.jackson.annotate.JsonProperty("content")
	private String content;

	@org.codehaus.jackson.annotate.JsonProperty("added_datetime")
	private String added_datetime;

	@org.codehaus.jackson.annotate.JsonProperty("is_web")
	private Boolean is_web;

	@org.codehaus.jackson.annotate.JsonProperty("send_by")
	private String send_by;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdded_datetime() {
		return added_datetime;
	}

	public void setAdded_datetime(String added_datetime) {
		this.added_datetime = added_datetime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSend_by() {
		return send_by;
	}

	public void setSend_by(String send_by) {
		this.send_by = send_by;
	}

	public Boolean Is_web() {
		return is_web;
	}

	public void setIs_web(Boolean is_web) {
		this.is_web = is_web;
	}
}