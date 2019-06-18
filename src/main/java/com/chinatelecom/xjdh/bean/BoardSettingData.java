package com.chinatelecom.xjdh.bean;

public class BoardSettingData {
	@org.codehaus.jackson.annotate.JsonProperty("cmd")
	private String cmd;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	@org.codehaus.jackson.annotate.JsonProperty("SERVER_ADDR")
	private String server_addr;
	@org.codehaus.jackson.annotate.JsonProperty("DEVICE_ID")
	private String device_id;
	@org.codehaus.jackson.annotate.JsonProperty("IP0")
	private String ip0;
	@org.codehaus.jackson.annotate.JsonProperty("NETMASK0")
	private String netmask0;
	@org.codehaus.jackson.annotate.JsonProperty("GATEWAY0")
	private String gateway0;
	@org.codehaus.jackson.annotate.JsonProperty("DNS0")
	private String dns0;
	@org.codehaus.jackson.annotate.JsonProperty("IP1")
	private String ip1;
	public String getServer_addr() {
		return server_addr;
	}
	public void setServer_addr(String server_addr) {
		this.server_addr = server_addr;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getIp0() {
		return ip0;
	}
	public void setIp0(String ip0) {
		this.ip0 = ip0;
	}
	public String getNetmask0() {
		return netmask0;
	}
	public void setNetmask0(String netmask0) {
		this.netmask0 = netmask0;
	}
	public String getGateway0() {
		return gateway0;
	}
	public void setGateway0(String gateway0) {
		this.gateway0 = gateway0;
	}
	public String getDns0() {
		return dns0;
	}
	public void setDns0(String dns0) {
		this.dns0 = dns0;
	}
	public String getIp1() {
		return ip1;
	}
	public void setIp1(String ip1) {
		this.ip1 = ip1;
	}
	public String getNetmask1() {
		return netmask1;
	}
	public void setNetmask1(String netmask1) {
		this.netmask1 = netmask1;
	}
	public String getGateway1() {
		return gateway1;
	}
	public void setGateway1(String gateway1) {
		this.gateway1 = gateway1;
	}
	public String getDns1() {
		return dns1;
	}
	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}
	@org.codehaus.jackson.annotate.JsonProperty("NETMASK1")
	private String netmask1;
	@org.codehaus.jackson.annotate.JsonProperty("GATEWAY1")
	private String gateway1;
	@org.codehaus.jackson.annotate.JsonProperty("DNS1")
	private String dns1;
	@Override
	public String toString() {
		return "BoardSettingData [cmd=" + cmd + ", server_addr=" + server_addr + ", device_id=" + device_id + ", ip0="
				+ ip0 + ", netmask0=" + netmask0 + ", gateway0=" + gateway0 + ", dns0=" + dns0 + ", ip1=" + ip1
				+ ", netmask1=" + netmask1 + ", gateway1=" + gateway1 + ", dns1=" + dns1 + "]";
	}
	
	
}
