package com.bakerbeach.market.cms.model;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface CmsContext {
	
	HttpServletRequest getHttpServletRequest();
	
	HttpServletResponse getHttpServletResponse();
	
	void setHttpServletRequest(HttpServletRequest httpServletRequest);
	
	void setHttpServletResponse(HttpServletResponse httpServletResponse);
	
	ModelMap getModelMap();
	
	void setModelMap(ModelMap modelMap);
	
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
	
	Map<String, Object> getData();

	Map<String, Object> getRequestData();

	Map<String, Object> getSessionData();

	CmsContext refine(UrlMappingInfo urlMappingInfo);
	
	String getHelperClass();
	
	String getDefaultPageId();

}
