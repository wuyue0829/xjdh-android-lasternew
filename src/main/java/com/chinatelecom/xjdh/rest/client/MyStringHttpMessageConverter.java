package com.chinatelecom.xjdh.rest.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;


public class MyStringHttpMessageConverter extends StringHttpMessageConverter {
	public MyStringHttpMessageConverter()
	{
		super(Charset.forName("UTF-8"));
	}
	
	private static final MediaType UTF8 = new MediaType("text", "plain",   
	           Charset.forName("UTF-8"));  
	  
	    private boolean writeAcceptCharset = true;  
	      
	
	  
	    @Override  
	    protected List<Charset> getAcceptedCharsets() {  
	        return Arrays.asList(UTF8.getCharSet());  
	    }  
	  
	    @Override  
	    protected MediaType getDefaultContentType(String t) throws IOException {  
	        return UTF8;  
	    }  
	  
	    public boolean isWriteAcceptCharset() {  
	        return writeAcceptCharset;  
	    }  
	  
	    public void setWriteAcceptCharset(boolean writeAcceptCharset) {  
	        this.writeAcceptCharset = writeAcceptCharset;  
	    }  
}
