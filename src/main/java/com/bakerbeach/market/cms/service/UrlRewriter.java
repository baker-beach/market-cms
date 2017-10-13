package com.bakerbeach.market.cms.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bakerbeach.market.cms.model.RequestContext;
import com.bakerbeach.market.cms.model.UrlMappingInfo;

public interface UrlRewriter {
	
	public UrlMappingInfo getRequestMapping(String url, Map<String, String[]> parameterMap, RequestContext cmsContext);

	public boolean processRequest(final HttpServletRequest request, final HttpServletResponse response, FilterChain parentChain) throws IOException, ServletException;

	public boolean processRequest(final HttpServletRequest request, final HttpServletResponse response, FilterChain parentChain, String path) throws IOException, ServletException;

}
