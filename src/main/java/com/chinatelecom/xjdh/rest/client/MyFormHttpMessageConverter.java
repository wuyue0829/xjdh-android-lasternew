package com.chinatelecom.xjdh.rest.client;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;

public class MyFormHttpMessageConverter extends FormHttpMessageConverter {
	@SuppressWarnings("unchecked")
	public MyFormHttpMessageConverter()
	{
		super();
		List converterList = new ArrayList<HttpMessageConverter<?>>();
		converterList.add(new ResourceHttpMessageConverter());
		converterList.add(new ByteArrayHttpMessageConverter());
		converterList.add(new MyStringHttpMessageConverter());
		this.setPartConverters(converterList);
	}
}
