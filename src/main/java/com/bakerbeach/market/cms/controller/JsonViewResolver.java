package com.bakerbeach.market.cms.controller;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

public class JsonViewResolver extends AbstractCachingViewResolver implements Ordered  {
	protected static Logger log = LoggerFactory.getLogger(Class.class.getName());
	
	private int order = Integer.MAX_VALUE;
	
	private View defaultView = null;
	
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		List<MediaType> requestedMediaTypes = resolveMediaTypes(((ServletRequestAttributes) attrs).getRequest());
		if (requestedMediaTypes != null) {
			if (requestedMediaTypes.contains(MediaType.APPLICATION_JSON)) {
				View view = defaultView;
				if (!StringUtils.equals(viewName, "json")) {
					try {
						view = (View) Class.forName(viewName).newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						log.error(ExceptionUtils.getStackTrace(e));
					}
				}
				
				return view;
			}
		}

		return null;
	}

	public List<MediaType> resolveMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {

		String header = request.getHeader(HttpHeaders.ACCEPT);
		if (StringUtils.isBlank(header)) {
			return Collections.emptyList();
		}
		try {
			List<MediaType> mediaTypes = MediaType.parseMediaTypes(header);
			MediaType.sortBySpecificityAndQuality(mediaTypes);
			return mediaTypes;
		} catch (InvalidMediaTypeException ex) {
			throw new HttpMediaTypeNotAcceptableException(
					"Could not parse 'Accept' header [" + header + "]: " + ex.getMessage());
		}
	}

	public void setDefaultView(View defaultView) {
		this.defaultView = defaultView;
	}
	
	@Override
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

}
