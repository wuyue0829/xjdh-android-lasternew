package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

/**
 * 申请资源
 * 
 * @author admin
 *
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimResoure implements Serializable {
	// {"error":"0","account":"13966666666","username":"aaa","deptname":"中国电信","email
	// ":"xxx@sina.com","telephone":"1234567","deptid":"11","mobile":"13966666666"}
	@org.codehaus.jackson.annotate.JsonProperty("error") // 错误码，等于0表示请求成功
	private String error;
	@org.codehaus.jackson.annotate.JsonProperty("account") // 用户账号,目前仅支持手机号。
	private String account;
	@org.codehaus.jackson.annotate.JsonProperty("username") // 用户的姓名
	private String username;
	@org.codehaus.jackson.annotate.JsonProperty("deptname") // 用户的组织机构
	private String deptname;
	@org.codehaus.jackson.annotate.JsonProperty("email") // 用户的邮箱
	private String email;
	@org.codehaus.jackson.annotate.JsonProperty("telephone") // 用户的电话
	private String telephone;
	@org.codehaus.jackson.annotate.JsonProperty("deptid") // 用户的组织机构id
	private String deptid;
	@org.codehaus.jackson.annotate.JsonProperty("mobile") // 用户的手机号
	private String mobile;

	@org.codehaus.jackson.annotate.JsonProperty("error_description")
	private String error_description;

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
