package com.bakerbeach.market.cms.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.UrlMappingInfo;

public class UrlRewriterImpl implements UrlRewriter {
	protected final Log logger = LogFactory.getLog(getClass());

	public static final String URL_MAPPING_INFO_KEY = "urlMappingInfo";

	private static final String DEFAULT_ENCODING = "UTF-8";

	private Boolean useEncodingHeader = true;
	private UrlService urlService;
	private String encoding = DEFAULT_ENCODING;

	public boolean processRequest(final HttpServletRequest request, final HttpServletResponse response,
			FilterChain parentChain) throws IOException, ServletException {
		String originalUri = getPathWithinApplication(request);
		return processRequest(request, response, parentChain, originalUri);
	}

	@SuppressWarnings("unchecked")
	public boolean processRequest(final HttpServletRequest request, final HttpServletResponse response,
			FilterChain parentChain, String originalUri) throws IOException, ServletException {
		CmsContext cmsCtx = CmsContextHolder.getInstance();

		UrlMappingInfo mapping = getRequestMapping(originalUri, (Map<String, String[]>) request.getParameterMap(),
				cmsCtx);
		if (mapping != null && mapping.getAction() != null) {
			if (mapping.getAction().equals("forward")) {
				Map<String, Object> data = mapping.getData();
				String location = (String) data.get("location");

				RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
				requestAttributes.setAttribute(URL_MAPPING_INFO_KEY, data, RequestAttributes.SCOPE_REQUEST);

				request.setAttribute("x-path", originalUri);
				request.getRequestDispatcher(location).forward(request, response);

				return true;
			} else if (mapping.getAction().equals("redirect")) {
				Map<String, Object> data = mapping.getData();

				String location = (String) data.get("location");
				location = new StringBuilder(request.getContextPath()).append(location).toString();

				response.setStatus((Integer) data.get("status"));
				response.setHeader("Location", location);
				response.setHeader("Connection", "close");

				return true;
			} else if (mapping.getAction().equals("handle")) {
				CmsContextHolder.getInstance().refine(mapping);
				return false;
			}
		}
		return false;
	}

	public UrlMappingInfo getRequestMapping(String url, Map<String, String[]> parameterMap, CmsContext cmsContext) {
		return urlService.getRequestMapping(url, parameterMap, cmsContext);
	}

	private String getPathWithinApplication(HttpServletRequest request) throws UnsupportedEncodingException {
		String requestUri = request.getRequestURI();
		String encoding = (useEncodingHeader) ? request.getCharacterEncoding() : this.encoding;

		if (requestUri == null) {
			requestUri = "";
		}

		String path = URLDecoder.decode(requestUri, encoding);
		String contextPath = getContextPath(request, encoding);
		if (StringUtils.startsWithIgnoreCase(path, contextPath)) {
			path = path.substring(contextPath.length());
		}

		return StringUtils.hasText(path) ? path : "/";
	}

	private String getContextPath(HttpServletRequest request, String encoding) throws UnsupportedEncodingException {
		String contextPath = request.getContextPath();
		if ("/".equals(contextPath)) {
			contextPath = "";
		}

		return URLDecoder.decode(contextPath, encoding);
	}

	public void reloadMappings() {

	}

	public UrlService getUrlService() {
		return urlService;
	}

	public void setUrlService(UrlService urlService) {
		this.urlService = urlService;
	}

}
