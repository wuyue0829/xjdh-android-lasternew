package com.chinatelecom.xjdh.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StationTable")
public class StationTable implements Serializable {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String stationName;// 局站名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
