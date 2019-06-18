package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class MemoList {
	
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private int id;
	
	@org.codehaus.jackson.annotate.JsonProperty("record")
	private String record;
	
	@org.codehaus.jackson.annotate.JsonProperty("user_id")
    private int user_id;
	
	@org.codehaus.jackson.annotate.JsonProperty("substation_id")
	private int substation_id;
	
	@org.codehaus.jackson.annotate.JsonProperty("substationName")
    private String substationName;
	
	@org.codehaus.jackson.annotate.JsonProperty("room_id")
	private int room_id;
	
	@org.codehaus.jackson.annotate.JsonProperty("roomName")
    private String roomName;
	
	@org.codehaus.jackson.annotate.JsonProperty("data_id")
	private String data_id;
	
	@org.codehaus.jackson.annotate.JsonProperty("deviceName")
    private String deviceName;
	
	@org.codehaus.jackson.annotate.JsonProperty("pics")
	private String pics;
	
	@org.codehaus.jackson.annotate.JsonProperty("active")
    private int active;
	
	@org.codehaus.jackson.annotate.JsonProperty("updated_at")
    private String updated_at;
	
	@org.codehaus.jackson.annotate.JsonProperty("content")
    private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getSubstation_id() {
		return substation_id;
	}

	public void setSubstation_id(int substation_id) {
		this.substation_id = substation_id;
	}

	public String getSubstationName() {
		return substationName;
	}

	public void setSubstationName(String substationName) {
		this.substationName = substationName;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getData_id() {
		return data_id;
	}

	public void setData_id(String data_id) {
		this.data_id = data_id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "MemoList [id=" + id + ", record=" + record + ", user_id=" + user_id + ", substation_id=" + substation_id
				+ ", substationName=" + substationName + ", room_id=" + room_id + ", roomName=" + roomName
				+ ", data_id=" + data_id + ", deviceName=" + deviceName + ", pics=" + pics + ", active=" + active
				+ ", updated_at=" + updated_at + ", content=" + content + "]";
	}



	
}
