package com.chinatelecom.xjdh.rest.interceptor;

import android.content.Context;

import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author peter
 * 
 */
@EBean
public class HttpBasicAuthenticatorInterceptor implements ClientHttpRequestInterceptor {
	@RootContext
	Context context;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution)
			throws IOException {
		ClientHttpResponse response = null;
		String token = PreferenceUtils.getPrefString(context, PreferenceConstants.ACCESSTOKEN, "");
		if (!token.isEmpty())
		request.getHeaders().set(SharedConst.HTTP_AUTHORIZATION, token);
		response = execution.execute(request, data);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		return response;

	}
}