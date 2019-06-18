package com.chinatelecom.xjdh.bean;
/**
 * @author peter
 * 
 */
public class UserDetailListItem {
	private String columnText;
	private String columnName;
	private String columnVal;
	/**
	 * 1-text 2-image
	 */
	private int type;
	private int id;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnVal() {
		return columnVal;
	}

	public void setColumnVal(String columnVal) {
		this.columnVal = columnVal;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getColumnText() {
		return columnText;
	}

	public void setColumnText(String columnText) {
		this.columnText = columnText;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserDetailListItem(int id, int type, String columnText, String columnName, String columnVal) {
		super();
		this.id = id;
		this.columnText = columnText;
		this.columnName = columnName;
		this.columnVal = columnVal;
		this.type = type;
	}

}
