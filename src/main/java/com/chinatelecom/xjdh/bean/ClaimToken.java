package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

/**
 * 申请令牌
 * 
 * @author admin
 *
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimToken implements Serializable {
	// {"error":"0","expires_in":2592000,"access_token":"9c7775f007a1bca79f37dffba483bf9f"}
	@org.codehaus.jackson.annotate.JsonProperty("error")
	private String error;
	@org.codehaus.jackson.annotate.JsonProperty("expires_in")
	private String expires_in;
	@org.codehaus.jackson.annotate.JsonProperty("access_token")
	private String access_token;
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

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
