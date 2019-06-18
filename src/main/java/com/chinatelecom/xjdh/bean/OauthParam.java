package com.chinatelecom.xjdh.bean;
/**
 * @author peter
 * 
 */
public class OauthParam {
	private String client_id;
	private String client_secret;
	private String redirect_uri;
	private String user_id;
	private String password;

	public OauthParam(String client_id, String client_secret, String redirect_uri, String user_id, String password) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.redirect_uri = redirect_uri;
		this.user_id = user_id;
		this.password = password;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
