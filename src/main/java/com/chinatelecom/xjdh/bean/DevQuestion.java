package com.chinatelecom.xjdh.bean;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class DevQuestion implements Parcelable{
	@org.codehaus.jackson.annotate.JsonProperty("device_id")
	private String device_id;
	@org.codehaus.jackson.annotate.JsonProperty("device")
	private String device;
	@org.codehaus.jackson.annotate.JsonProperty("image")
	private String[] content;
	@org.codehaus.jackson.annotate.JsonProperty("devList")
	private DevItem[] devList;
	@org.codehaus.jackson.annotate.JsonProperty("name")
	private String name;
	@org.codehaus.jackson.annotate.JsonProperty("type")
	private String type;

	
	
	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	public DevItem[] getDevList() {
		return devList;
	}

	public void setDevList(DevItem[] devList) {
		this.devList = devList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(type);
		out.writeString(name);
		out.writeParcelableArray(devList, flags);
	}

	public static final Creator<DevQuestion> CREATOR = new Creator<DevQuestion>() {
		@Override
		public DevQuestion[] newArray(int size) {
			return new DevQuestion[size];
		}

		@Override
		public DevQuestion createFromParcel(Parcel in) {
			return new DevQuestion(in);
		}
	};

	public DevQuestion() {
		super();
	}

	public DevQuestion(Parcel in) {
		type = in.readString();
		name = in.readString();
		Parcelable[] parcelables = in.readParcelableArray(DevItem.class.getClassLoader());
		if (parcelables != null) {
			devList = Arrays.copyOf(parcelables, parcelables.length, DevItem[].class);
		}
	}

	
}
