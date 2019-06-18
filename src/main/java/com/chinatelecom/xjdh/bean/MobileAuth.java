package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class MobileAuth implements Serializable{
	@org.codehaus.jackson.annotate.JsonProperty("data_report")
	private String data_report;
	@org.codehaus.jackson.annotate.JsonProperty("sub_collect")
	private String sub_collect;
	@org.codehaus.jackson.annotate.JsonProperty("sub_list")
	private String sub_list;
	@org.codehaus.jackson.annotate.JsonProperty("wifi_test")
	private String wifi_test;
	@org.codehaus.jackson.annotate.JsonProperty("sub_check")
	private String sub_check;
	@org.codehaus.jackson.annotate.JsonProperty("read_card")
	private String read_card;
	@org.codehaus.jackson.annotate.JsonProperty("realtimedata")
	private String realtimedata;
	@org.codehaus.jackson.annotate.JsonProperty("pre_alarm")
	private String pre_alarm;
	@org.codehaus.jackson.annotate.JsonProperty("alarm")
	private String alarm;
	@org.codehaus.jackson.annotate.JsonProperty("bluetooth_test")
	private String bluetooth_test;
	@org.codehaus.jackson.annotate.JsonProperty("message")
	private String message;
	public String getData_report() {
		return data_report;
	}
	public void setData_report(String data_report) {
		this.data_report = data_report;
	}
	public String getSub_collect() {
		return sub_collect;
	}
	public void setSub_collect(String sub_collect) {
		this.sub_collect = sub_collect;
	}
	public String getSub_list() {
		return sub_list;
	}
	public void setSub_list(String sub_list) {
		this.sub_list = sub_list;
	}
	public String getWifi_test() {
		return wifi_test;
	}
	public void setWifi_test(String wifi_test) {
		this.wifi_test = wifi_test;
	}
	public String getSub_check() {
		return sub_check;
	}
	public void setSub_check(String sub_check) {
		this.sub_check = sub_check;
	}
	public String getRead_card() {
		return read_card;
	}
	public void setRead_card(String read_card) {
		this.read_card = read_card;
	}
	public String getRealtimedata() {
		return realtimedata;
	}
	public void setRealtimedata(String realtimedata) {
		this.realtimedata = realtimedata;
	}
	public String getPre_alarm() {
		return pre_alarm;
	}
	public void setPre_alarm(String pre_alarm) {
		this.pre_alarm = pre_alarm;
	}
	public String getAlarm() {
		return alarm;
	}
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getBluetooth_test() {
		return bluetooth_test;
	}
	public void setBluetooth_test(String bluetooth_test) {
		this.bluetooth_test = bluetooth_test;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MobileAuth [data_report=" + data_report + ", sub_collect=" + sub_collect + ", sub_list=" + sub_list
				+ ", wifi_test=" + wifi_test + ", sub_check=" + sub_check + ", read_card=" + read_card
				+ ", realtimedata=" + realtimedata + ", pre_alarm=" + pre_alarm + ", alarm=" + alarm
				+ ", bluetooth_test=" + bluetooth_test + ", message=" + message + "]";
	}
	
	
	

}
