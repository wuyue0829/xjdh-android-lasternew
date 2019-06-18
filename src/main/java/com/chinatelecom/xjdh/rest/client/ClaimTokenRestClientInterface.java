package com.chinatelecom.xjdh.rest.client;

import java.util.LinkedHashMap;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ClaimOpenId;
import com.chinatelecom.xjdh.bean.ClaimResoure;
import com.chinatelecom.xjdh.bean.ClaimToken;
import com.chinatelecom.xjdh.utils.URLs;

/**
 * @author peter
 * 
 */
@Rest(rootUrl = URLs.CLAIMTOKEN, converters = { MappingJacksonHttpMessageConverter.class,
		StringHttpMessageConverter.class, FormHttpMessageConverter.class, ResourceHttpMessageConverter.class,
		ByteArrayHttpMessageConverter.class })
@RequiresHeader({ "Content-Type" })
public interface ClaimTokenRestClientInterface extends RestClientHeaders {
	@Post("/OAuthCenter/accessToken")
	ClaimToken accessToken(MultiValueMap<String, Object> items) throws RestClientException;

	@Get("/OAuthCenter/me?access_token={access_token}&client_id={client_id}")
	ClaimOpenId me(String access_token, String client_id) throws RestClientException;;

	@Post("/OAuthCenter/v1/api/get_basicUserInfo")
	ClaimResoure get_basicUserInfo(MultiValueMap<String, Object> resource) throws RestClientException;
	@Post("/creationUser")
	ApiResponse creationUser(LinkedHashMap<String, String> items) throws RestClientException;
}
