package com.chinatelecom.xjdh.rest.client;

import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.chinatelecom.xjdh.utils.URLs;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;

import java.util.LinkedHashMap;

/**
 * @author peter
 * 
 */
@Rest(rootUrl = URLs.URL_API_HOST + "/oauth2", converters = { MappingJacksonHttpMessageConverter.class,
		StringHttpMessageConverter.class, MyFormHttpMessageConverter.class, MyStringHttpMessageConverter.class,
		ResourceHttpMessageConverter.class, FormHttpMessageConverter.class,
		ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface OauthRestClientInterface extends RestClientHeaders {
	@Post("/authenticate")
	LoginResponse login(OauthParam param) throws RestClientException;
	@Post("/authenticate")
	ApiResponse creationUser(LinkedHashMap<String, String> items) throws RestClientException;
}
