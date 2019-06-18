package com.chinatelecom.xjdh.bean;

import java.io.Serializable;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class StationImg implements Serializable {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("stationImage")
	private String stationImage;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStationImage() {
		return stationImage;
	}
	public void setStationImage(String stationImage) {
		this.stationImage = stationImage;
	}
	
}
