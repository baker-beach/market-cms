package com.bakerbeach.market.cms.model;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.ldap.LdapReferralException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.bakerbeach.market.cms.service.Helper;

public class CmsContextImpl implements CmsContext {
	
	private String helperClass = Helper.class.getName();
	private String appCode;
	private String host;
	private String path;
	private Integer port;
	private Integer securePort;
	private String pageId;
	private List<Locale> locales;
	private Locale defaultLocale;
	private Locale currentLocale;
	private String protocol;
	private HttpServletRequest httpServletRequest;
	private HttpServletResponse httpServletResponse;
	private Map<String, Object> data = new HashMap<String, Object>();
	private ModelMap modelMap;
	private String defaultPageId;
	private Map<String, Object> requestData = new HashMap<String, Object>();
	private Map<String, Object> sessionData = new HashMap<String, Object>();
	
	public CmsContextImpl(CmsContext cmsContext){
		setAppCode(cmsContext.getAppCode());
		setPort(cmsContext.getPort());
		setSecurePort(cmsContext.getSecurePort());
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getSecurePort() {
		return securePort;
	}

	public void setSecurePort(Integer securePort) {
		this.securePort = securePort;
	}

	public String getPageId() {
		return pageId;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public List<Locale> getLocales() {
		return locales;
	}

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CmsContext refine(UrlMappingInfo urlMappingInfo) {
		if (urlMappingInfo != null) {
			String pageId = (String) urlMappingInfo.get("page_id");
			setPageId(pageId);
			setData((Map<String, Object>) urlMappingInfo.get("data"));
		}

		return this;
	}

	@Deprecated
	public Map<String, Object> getData() {
		return getRequestData();
	}

	@Deprecated
	public void setData(Map<String, Object> data) {
		setReguestData(data);
	}

	@Override
	public Map<String, Object> getRequestData() {
		return requestData;
	}
	
	public void setReguestData(Map<String, Object> reguestData) {
		this.requestData = reguestData;
	}
	
	@Override
	public Map<String, Object> getSessionData() {
		return sessionData;
	}
	
	public void setSessionData(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
	
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}

	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public ModelMap getModelMap() {
		return modelMap;
	}

	public void setModelMap(ModelMap modelMap) {
		this.modelMap = modelMap;
	}


	public String getHelperClass() {
		return helperClass;
	}

	public String getDefaultPageId() {
		return defaultPageId;
	}

	public void setDefaultPageId(String defaultPageId) {
		this.defaultPageId = defaultPageId;
	}
}
