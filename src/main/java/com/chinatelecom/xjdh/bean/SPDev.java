package com.chinatelecom.xjdh.bean;

public class SPDev {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private int baud_rate;
	public int getBaud_rate() {
		return baud_rate;
	}
	public void setBaud_rate(int baud_rate) {
		this.baud_rate = baud_rate;
	}
	private String name;
	private String cmd;
	private String reply;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
}
