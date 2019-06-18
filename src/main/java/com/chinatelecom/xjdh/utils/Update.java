package com.chinatelecom.xjdh.utils;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * 应用程序更新实体类
 * 
 * @author peter
 * @version 1.1
 * @created 2015-07-08
 */
public class Update implements Serializable {
	private static final long serialVersionUID = -4181397418247151521L;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("version_code")
	private Integer versionCode;
	@JsonProperty("version_name")
	private String versionName;
	@JsonProperty("download_url")
	private String downloadUrl;
	@JsonProperty("update_log")
	private String updateLog;
	@JsonProperty("update_datetime")
	private String updateDatetime;

	public String getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
}
