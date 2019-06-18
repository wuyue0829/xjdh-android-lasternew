package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

public class EnergyData {
	
	@org.codehaus.jackson.annotate.JsonProperty("orderValue")
	private int orderValue[];
	@org.codehaus.jackson.annotate.JsonProperty("ryuValue")
	private double ryuValue[];
	@org.codehaus.jackson.annotate.JsonProperty("rpgValue")
	private double rpgValue[];
	@org.codehaus.jackson.annotate.JsonProperty("pxValue")
	private double pxValue[];
	@org.codehaus.jackson.annotate.JsonProperty("uxRmsValue")
	private double uxRmsValue[];	
	@org.codehaus.jackson.annotate.JsonProperty("ixRmsValue")
	private double ixRmsValue[];
	@org.codehaus.jackson.annotate.JsonProperty("pfxValue")
	private double pfxValue[];
	@org.codehaus.jackson.annotate.JsonProperty("freValue")
	private double freValue[];	
	@org.codehaus.jackson.annotate.JsonProperty("esxValue")
	private double esxValue[];
	
	public int[] getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(int[] orderValue) {
		this.orderValue = orderValue;
	}
	public double[] getRyuValue() {
		return ryuValue;
	}
	public void setRyuValue(double[] ryuValue) {
		this.ryuValue = ryuValue;
	}
	public double[] getRpgValue() {
		return rpgValue;
	}
	public void setRpgValue(double[] rpgValue) {
		this.rpgValue = rpgValue;
	}
	public double[] getPxValue() {
		return pxValue;
	}
	public void setPxValue(double[] pxValue) {
		this.pxValue = pxValue;
	}
	public double[] getUxRmsValue() {
		return uxRmsValue;
	}
	public void setUxRmsValue(double[] uxRmsValue) {
		this.uxRmsValue = uxRmsValue;
	}
	public double[] getIxRmsValue() {
		return ixRmsValue;
	}
	public void setIxRmsValue(double[] ixRmsValue) {
		this.ixRmsValue = ixRmsValue;
	}
	public double[] getPfxValue() {
		return pfxValue;
	}
	public void setPfxValue(double[] pfxValue) {
		this.pfxValue = pfxValue;
	}
	public double[] getFreValue() {
		return freValue;
	}
	public void setFreValue(double[] freValue) {
		this.freValue = freValue;
	}
	public double[] getEsxValue() {
		return esxValue;
	}
	public void setEsxValue(double[] esxValue) {
		this.esxValue = esxValue;
	}
	@Override
	public String toString() {
		return "EnergyData [pxValue=" + Arrays.toString(pxValue) + ", uxRmsValue=" + Arrays.toString(uxRmsValue)
				+ ", ixRmsValue=" + Arrays.toString(ixRmsValue) + ", pfxValue=" + Arrays.toString(pfxValue)
				+ ", freValue=" + Arrays.toString(freValue) + ", esxValue=" + Arrays.toString(esxValue) + "]";
	}
	
}
