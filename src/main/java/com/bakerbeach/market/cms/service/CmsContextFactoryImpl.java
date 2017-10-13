package com.bakerbeach.market.cms.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bakerbeach.market.cms.model.RequestContext;
import com.bakerbeach.market.cms.model.CmsContextImpl;

public class CmsContextFactoryImpl implements CmsContextFactory {

	private Map<String, RequestContext> contextDefinitions;

	public void setContextDefinitions(Map<String, RequestContext> contextDefinitions) {
		this.contextDefinitions = contextDefinitions;
	}


	public RequestContext newInstance(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws CmsContextException {
		try {
			
			String host = UrlHelper.getHost(httpServletRequest);
			
			RequestContext contextDefinition = contextDefinitions.get(host);
			CmsContextImpl cmsContext = new CmsContextImpl(contextDefinition);
			
			cmsContext.setHost(host);
			cmsContext.setPath(UrlHelper.getPathWithinApplication(httpServletRequest));
			cmsContext.setProtocol(UrlHelper.getProtocol(httpServletRequest));
			cmsContext.setHttpServletRequest(httpServletRequest);
			cmsContext.setHttpServletResponse(httpServletResponse);
			
			return cmsContext;
		} catch (Exception e) {
			throw new CmsContextException(e);
		}
	}
	
}
