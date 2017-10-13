package com.bakerbeach.market.cms.model;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

@SuppressWarnings("unused")
public interface RequestContext {
	
	String getAppCode();
	
	String getPageId();
	
	Locale getCurrentLocale();

	void setHost(String host);

	void setPath(String path);

	void setProtocol(String protocol);

	void setPageId(String pageId);
	
	String getHost();

	String getPath();

	String getProtocol();
	
	Integer getPort();
	
	Integer getSecurePort();

	String getHelperClass();
	
	String getDefaultPageId();
	
	HttpServletRequest getHttpServletRequest();
	
	HttpServletResponse getHttpServletResponse();
	
	ModelMap getModelMap();
	
	RequestContext refine(UrlMappingInfo urlMappingInfo);

	Map<String, Object> getRequestData();

}
