package com.chinatelecom.xjdh.bean;

public class ChangePwd {
	private String pwdold;
	private String pwdnew;
	private String pwdagin;
	public String getPwdold() {
		return pwdold;
	}
	public void setPwdold(String pwdold) {
		this.pwdold = pwdold;
	}
	public String getPwdnew() {
		return pwdnew;
	}
	public void setPwdnew(String pwdnew) {
		this.pwdnew = pwdnew;
	}
	public String getPwdagin() {
		return pwdagin;
	}
	public void setPwdagin(String pwdagin) {
		this.pwdagin = pwdagin;
	}
	public ChangePwd(String pwdold, String pwdnew, String pwdagin) {
		super();
		this.pwdold = pwdold;
		this.pwdnew = pwdnew;
		this.pwdagin = pwdagin;
	}
	
	
	

}
