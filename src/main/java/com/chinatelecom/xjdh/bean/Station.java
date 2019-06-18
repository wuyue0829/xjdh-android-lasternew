package com.chinatelecom.xjdh.bean;

public class Station {
	@org.codehaus.jackson.annotate.JsonProperty("substation_id")
	private int substation_id;
	@org.codehaus.jackson.annotate.JsonProperty("substationName")
    private String substationName;
	public int getSubstation_id() {
		return substation_id;
	}
	public void setSubstation_id(int substation_id) {
		this.substation_id = substation_id;
	}
	public String getSubstationName() {
		return substationName;
	}
	public void setSubstationName(String substationName) {
		this.substationName = substationName;
	}
	@Override
	public String toString() {
		return "Station [substation_id=" + substation_id + ", substationName=" + substationName + "]";
	}
	
	
}
