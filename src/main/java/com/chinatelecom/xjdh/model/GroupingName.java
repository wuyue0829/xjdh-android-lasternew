package com.chinatelecom.xjdh.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GroupingName")
public class GroupingName implements Serializable {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String groupingName;// 分组名称
	@DatabaseField()
	private String stationName;// 局站名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupingName() {
		return groupingName;
	}

	public void setGroupingName(String groupingName) {
		this.groupingName = groupingName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
