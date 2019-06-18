package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class BoardJsonData {
	@org.codehaus.jackson.annotate.JsonProperty("di")
	private int di[];
	@org.codehaus.jackson.annotate.JsonProperty("ai")
	private short ai[];	
	@org.codehaus.jackson.annotate.JsonProperty("do")
	private int d_o[];
	public int[] getDi() {
		return di;
	}
	public void setDi(int[] di) {
		this.di = di;
	}
	public short[] getAi() {
		return ai;
	}
	public void setAi(short[] ai) {
		this.ai = ai;
	}
	public int[] getD_o() {
		return d_o;
	}
	public void setD_o(int[] d_o) {
		this.d_o = d_o;
	}
	@Override
	public String toString() {
		return "BoardJsonData [di=" + Arrays.toString(di) + ", ai=" + Arrays.toString(ai) + ", d_o="
				+ Arrays.toString(d_o) + "]";
	}
	
	
	
}
