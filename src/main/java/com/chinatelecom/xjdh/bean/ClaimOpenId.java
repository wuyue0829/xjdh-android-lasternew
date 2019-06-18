package com.chinatelecom.xjdh.bean;

import java.io.Serializable;

/**
 * 申请openId
 * 
 * @author admin
 *
 */
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimOpenId implements Serializable {
	// {"error":"0","client_id":"jj","open_id":"CTINM_B983FFD82CEB721481A5A6EB7A16036D"}
	@org.codehaus.jackson.annotate.JsonProperty("error")
	private String error;
	@org.codehaus.jackson.annotate.JsonProperty("client_id")
	private String client_id;
	@org.codehaus.jackson.annotate.JsonProperty("open_id")
	private String open_id;
	@org.codehaus.jackson.annotate.JsonProperty("error_description")
	private String error_description;
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

}
