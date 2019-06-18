package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class StationImage implements Serializable {
	@org.codehaus.jackson.annotate.JsonProperty("stationImage")
	private String stationImage[];

	public String[] getStationImage() {
		return stationImage;
	}

	public void setStationImage(String[] stationImage) {
		this.stationImage = stationImage;
	}

}
