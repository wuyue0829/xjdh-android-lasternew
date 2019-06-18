package com.chinatelecom.xjdh.bean;

public class CmdResult {
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	private String cmd;
	private String result;
	@Override
	public String toString() {
		return "CmdResult [cmd=" + cmd + ", result=" + result + "]";
	}
	
}
