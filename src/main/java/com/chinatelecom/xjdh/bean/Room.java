package com.chinatelecom.xjdh.bean;

public class Room {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private int room_code;
	@org.codehaus.jackson.annotate.JsonProperty("name")
	private String room_name;
	public int getRoom_code() {
		return room_code;
	}
	public void setRoom_code(int room_code) {
		this.room_code = room_code;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	@Override
	public String toString() {
		return "Room [room_code=" + room_code + ", room_name=" + room_name + "]";
	}
	
	
}
