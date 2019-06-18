package com.chinatelecom.xjdh.bean;

import android.util.Log;

public class RtspUrl {
	private String url;
	private String name;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RtspUrl() {
		super();
	}
	@Override
	public String toString() {
		Log.i("::::::::", "RtspUrl [url=" + url + ", name=" + name + "]");
		return "RtspUrl [url=" + url + ", name=" + name + "]";
	}
	public RtspUrl(String url, String name) {
		super();
		this.url = url;
		this.name = name;
	}

	
	
}
