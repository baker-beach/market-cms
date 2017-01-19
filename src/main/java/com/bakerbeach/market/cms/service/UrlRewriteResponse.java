package com.bakerbeach.market.cms.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class UrlRewriteResponse extends HttpServletResponseWrapper {
	private UrlRewriter rewriter;
	private HttpServletResponse httpServletResponse;
	private HttpServletRequest httpServletRequest;
	String overridenMethod;

	public UrlRewriteResponse(HttpServletResponse httpServletResponse,
			HttpServletRequest httpServletRequest, UrlRewriter rewriter) {
		super(httpServletResponse);
		this.httpServletResponse = httpServletResponse;
		this.httpServletRequest = httpServletRequest;
		this.rewriter = rewriter;
	}
	
	public String encodeURL(String url) {
		return super.encodeUrl(url);
	}

}
