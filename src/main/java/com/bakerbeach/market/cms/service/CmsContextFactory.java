package com.bakerbeach.market.cms.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bakerbeach.market.cms.model.RequestContext;

public interface CmsContextFactory {
	
	RequestContext newInstance(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws CmsContextException;

}
