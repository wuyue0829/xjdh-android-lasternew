package com.chinatelecom.xjdh.bean;

import java.util.List;

public class SPDevResponse {
	private int ret;
	private List<SPDev> spdevList;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public List<SPDev> getSpdevList() {
		return spdevList;
	}
	public void setSpdevList(List<SPDev> spdevList) {
		this.spdevList = spdevList;
	}
}
