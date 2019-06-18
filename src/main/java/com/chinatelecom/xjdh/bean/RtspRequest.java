package com.chinatelecom.xjdh.bean;

import android.os.Parcel;
import android.os.Parcelable;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class RtspRequest implements Parcelable{
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private String id;
	@org.codehaus.jackson.annotate.JsonProperty("data_id")
	private String data_id;
	@org.codehaus.jackson.annotate.JsonProperty("name")
	private String name;
	@org.codehaus.jackson.annotate.JsonProperty("added_datetime")
	private String added_datetime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdded_datetime() {
		return added_datetime;
	}
	public void setAdded_datetime(String added_datetime) {
		this.added_datetime = added_datetime;
	}
	@Override
	public String toString() {
		return "RtspRequest [id=" + id + ", data_id=" + data_id + ", name=" + name + ", added_datetime="
				+ added_datetime + "]";
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(data_id);
		dest.writeString(added_datetime);
	}
	
}
