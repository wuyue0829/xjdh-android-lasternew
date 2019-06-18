package com.chinatelecom.xjdh.bean;

public class RoomSubs {
	@org.codehaus.jackson.annotate.JsonProperty("room_id")
	private int room_id;
	@org.codehaus.jackson.annotate.JsonProperty("room_name")
	private String room_name;
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	@Override
	public String toString() {
		return "RoomSubs [room_id=" + room_id + ", room_name=" + room_name + "]";
	}
	
	
}
