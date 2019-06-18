package com.chinatelecom.xjdh.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "FileNameGPSTable")
public class FileNameGPSTable {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String fileName;// 文件名(图片名)
	@DatabaseField
	private String longitude;// 经度
	@DatabaseField
	private String latitude;// 纬度
	@DatabaseField
	private String stationName;// 局站名称
	
	@DatabaseField
	private String groupingName;// 分组名称
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getGroupingName() {
		return groupingName;
	}

	public void setGroupingName(String groupingName) {
		this.groupingName = groupingName;
	}

}
