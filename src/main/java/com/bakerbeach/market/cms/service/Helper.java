package com.bakerbeach.market.cms.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bakerbeach.market.translation.api.service.TranslationService;

public class Helper{
	protected static final Logger LOG = LoggerFactory.getLogger(Helper.class.getName());
	
	@Autowired
	protected UrlService urlService;
	
	@Autowired
	protected VelocityEngine velocityEngine;
	
	@Autowired
	protected TranslationService translationService;

	public static String out(String in) {
		return StringEscapeUtils.escapeHtml(in);
	}

	public static String json(Object in) {
		try {
			StringWriter writer = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, in);
			return writer.toString();
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			return "";
		}
	}

	public static String resourceUrl(String key) {
		StringBuilder path = new StringBuilder(CmsContextHolder.getInstance().getHttpServletRequest().getContextPath());
		path.append("/resources");
		path.append(key);
		return path.toString();
	}
	
	public String t(String code) {
		if (code != null) {
			return t("text", "default", code, code, CmsContextHolder.getInstance().getCurrentLocale());
		} else {
			return null;
		}
	}

	public String t(String tag, String code, Object... args) {
		if (code != null) {
			return t("text", tag, code, code, CmsContextHolder.getInstance().getCurrentLocale(), args);
		} else {
			return null;
		}
	}

	public String tu(String tag, String code, Object... args) {
		if (code != null) {
			return t("url", tag, code, code, CmsContextHolder.getInstance().getCurrentLocale(), args);
		} else {
			return null;
		}
	}
	
	public String t(String type, String tag, String code, Object... args) {
		if (code != null) {
			return t(type, tag, code, code, CmsContextHolder.getInstance().getCurrentLocale(), args);		
		} else {
			return null;
		}
	}
	
	public String t(String type, String tag, String code, String def, Locale locale, Object... args) {
		try{
			String msg = translationService.getMessage(tag, type, code, args, def, locale);
			return render(msg);
		} catch (Exception e){
			LOG.error(e.getMessage());
			return "";
		}
	}

	public static String f(String pattern, Object... args) {
		return String.format(CmsContextHolder.getInstance().getCurrentLocale(), pattern, args);
	}

	public static String f(Date date, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, CmsContextHolder.getInstance().getCurrentLocale());
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String f(LocalDateTime dateTime, String pattern) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, CmsContextHolder.getInstance().getCurrentLocale());
			return dateTime.format(formatter);
		} catch (Exception e) {
			return "";
		}
	}
	
	public String pageUrl(String pageId) {
		String uri = urlService.getPageUrl(pageId, CmsContextHolder.getInstance().getAppCode(), CmsContextHolder.getInstance().getCurrentLocale().getLanguage());
		if(uri == null)
			uri = "/";
		return url(uri);
	}

	public String pageUri(String pageId) {
		String uri = urlService.getPageUrl(pageId, CmsContextHolder.getInstance().getAppCode(), CmsContextHolder.getInstance().getCurrentLocale().getLanguage());
		if(uri == null)
			uri = "/";
		return uri;
	}


	public static String b64(String in) {
		return Base64.encodeBase64URLSafeString(in.getBytes());
	}

	private static String uri(String key) {
		String encodedUrl = CmsContextHolder.getInstance().getHttpServletResponse().encodeURL(key);
		return new StringBuilder(CmsContextHolder.getInstance().getHttpServletRequest().getContextPath()).append(encodedUrl).toString();
	}



	private static String url(String protocol, String host, Integer port, String key) {
		if (port == 80 || port == 443) {
			return new StringBuilder(protocol).append("://").append(host).append(uri(key)).toString();
		} else {
			return new StringBuilder(protocol).append("://").append(host).append(":").append(port).append(uri(key)).toString();
		}
	}

	public static String url(String key) {
		String protocol = (CmsContextHolder.getInstance().getHttpServletRequest().isSecure()) ? "https" : "http";
		return url(protocol, key);
	}

	private static String url(String protocol, String key) {
		String host = CmsContextHolder.getInstance().getHttpServletRequest().getServerName();
		Integer port = ("http".equals(protocol)) ? CmsContextHolder.getInstance().getPort() : CmsContextHolder.getInstance().getSecurePort();
		return url(protocol, host, port, key);
	}

	public static String getRawUri() {
		return CmsContextHolder.getInstance().getHttpServletRequest().getRequestURI();
	}

	public static String getCurrentUrl() {
		StringBuffer requestUrl = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRequestURL();
		String queryString = CmsContextHolder.getInstance().getHttpServletRequest().getQueryString();
		if (queryString != null && !queryString.isEmpty())
			requestUrl.append("?").append(queryString);
		return requestUrl.toString();
	}

	public String render(String text) {
		try {
			StringWriter stringWriter = new StringWriter();
			velocityEngine.evaluate(new VelocityContext(CmsContextHolder.getInstance().getModelMap()), stringWriter, "post_message_rendere", new StringReader(text));
			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

}
