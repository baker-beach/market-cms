package com.bakerbeach.market.cms.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class UrlHelper {

	public static String getPathWithinApplication(HttpServletRequest request) {
		try {
			String requestUri = request.getRequestURI();
			String encoding = request.getCharacterEncoding();

			if (requestUri == null) {
				requestUri = "";
			}

			String path = URLDecoder.decode(requestUri, encoding);
			String contextPath = getContextPath(request, encoding);
			if (StringUtils.startsWithIgnoreCase(path, contextPath)) {
				path = path.substring(contextPath.length());
			}

			return StringUtils.hasText(path) ? path : "/";
		} catch (Exception E) {
			return "/";
		}
	}

	public static String getContextPath(HttpServletRequest request, String encoding) throws UnsupportedEncodingException {
		String contextPath = request.getContextPath();
		if ("/".equals(contextPath)) {
			contextPath = "";
		}

		return URLDecoder.decode(contextPath, encoding);
	}
	
	public static String getHost(HttpServletRequest request) {
		return request.getServerName();
	}
	
	public static String getProtocol(HttpServletRequest request) {
		return request.getProtocol().split("/")[0];
	}

}
