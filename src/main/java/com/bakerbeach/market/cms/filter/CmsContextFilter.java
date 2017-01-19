package com.bakerbeach.market.cms.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.service.CmsContextFactory;
import com.bakerbeach.market.cms.service.CmsContextHolder;
import com.bakerbeach.market.cms.service.UrlHelper;

public class CmsContextFilter extends AbstractContextFilter {
	private static final Logger log = LoggerFactory.getLogger(CmsContextFilter.class.getName());

	private CmsContextFactory cmsContextFactory;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (skipFilter(httpServletRequest)) {
			chain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			try {
				String host = UrlHelper.getHost(httpServletRequest);
				String path = UrlHelper.getPathWithinApplication(httpServletRequest);

				CmsContext cmsContext = CmsContextHolder.getInstance();
				if (cmsContext == null) {
					cmsContext = cmsContextFactory.newInstance(host, path);
					cmsContext.setProtocol(UrlHelper.getProtocol(httpServletRequest));
					CmsContextHolder.setInstance(cmsContext);
				} else {
					cmsContext.setHost(host);
					cmsContext.setPath(path);
					cmsContext.setProtocol(UrlHelper.getProtocol(httpServletRequest));
					cmsContext.setPageId(null);
				}
				
				chain.doFilter(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new ServletException(e);
			}
		}
	}

	public void setShopContextFactory(CmsContextFactory cmsContextFactory) {
		this.cmsContextFactory = cmsContextFactory;
	}

}
