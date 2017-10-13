package com.bakerbeach.market.cms.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bakerbeach.market.cms.model.RequestContext;
import com.bakerbeach.market.translation.api.service.TranslationService;

@Component
@Scope("prototype")
public class Helper {
	protected static final Logger LOG = LoggerFactory.getLogger(Helper.class.getName());

	@Autowired
	protected UrlService urlService;

	@Autowired
	protected VelocityEngine velocityEngine;

	@Autowired
	protected TranslationService translationService;
	
	protected RequestContext context;
	
	public Helper(RequestContext context) {
		this.context = context; 
	}
	
	public String out(String in) {
		return StringEscapeUtils.escapeHtml(in);
	}

	public String json(Object in) {
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

	public String resourceUrl(String key) {
		StringBuilder path = new StringBuilder(context.getHttpServletRequest().getContextPath());
		path.append("/resources");
		path.append(key);
		return path.toString();
	}

	public String t(String code) {
		if (code != null) {
			return t("text", "default", code, code, context.getCurrentLocale());
		} else {
			return null;
		}
	}

	public String t(String tag, String code, Object... args) {
		if (code != null) {
			return t("text", tag, code, code, context.getCurrentLocale(), args);
		} else {
			return null;
		}
	}

	public String tu(String tag, String code, Object... args) {
		if (code != null) {
			return t("url", tag, code, code, context.getCurrentLocale(), args);
		} else {
			return null;
		}
	}

	public String t(String type, String tag, String code, Object... args) {
		if (code != null) {
			return t(type, tag, code, code, context.getCurrentLocale(), args);
		} else {
			return null;
		}
	}

	public String t(String type, String tag, String code, String def, Locale locale, Object... args) {
		try {
			String msg = translationService.getMessage(tag, type, code, args, def, locale);
			return render(msg);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return "";
		}
	}

	public String f(String pattern, Object... args) {
		return String.format(context.getCurrentLocale(), pattern, args);
	}

	public String f(Date date, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, context.getCurrentLocale());
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public String f(LocalDateTime dateTime, String pattern) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, context.getCurrentLocale());
			return dateTime.format(formatter);
		} catch (Exception e) {
			return "";
		}
	}

	public String pageUrl(String pageId) {
		String uri = urlService.getPageUrl(pageId, context.getAppCode(), context.getCurrentLocale().getLanguage());
		if (uri == null)
			uri = "/";
		return url(uri);
	}

	public String pageUri(String pageId) {
		String uri = urlService.getPageUrl(pageId, context.getAppCode(), context.getCurrentLocale().getLanguage());
		if (uri == null)
			uri = "/";
		return uri;
	}

	public static String b64(String in) {
		return Base64.encodeBase64URLSafeString(in.getBytes());
	}

	private String uri(String key) {
		String encodedUrl = context.getHttpServletResponse().encodeURL(key);
		return new StringBuilder(context.getHttpServletRequest().getContextPath()).append(encodedUrl).toString();
	}

	private String url(String protocol, String host, Integer port, String key) {
		if (port == 80 || port == 443) {
			return new StringBuilder(protocol).append("://").append(host).append(uri(key)).toString();
		} else {
			return new StringBuilder(protocol).append("://").append(host).append(":").append(port).append(uri(key)).toString();
		}
	}

	public String url(String key) {
		String protocol = (context.getHttpServletRequest().isSecure()) ? "https" : "http";
		return url(protocol, key);
	}

	private String url(String protocol, String key) {
		String host = context.getHttpServletRequest().getServerName();
		Integer port = ("http".equals(protocol)) ? context.getPort() : context.getSecurePort();
		return url(protocol, host, port, key);
	}

	public String getRawUri() {
		return context.getHttpServletRequest().getRequestURI();
	}

	public String getCurrentUrl() {
		StringBuffer requestUrl = context.getHttpServletRequest().getRequestURL();
		String queryString = context.getHttpServletRequest().getQueryString();
		if (queryString != null && !queryString.isEmpty())
			requestUrl.append("?").append(queryString);
		return requestUrl.toString();
	}

	public String render(String text) {
		try {
			StringWriter stringWriter = new StringWriter();
			velocityEngine.evaluate(new VelocityContext(context.getModelMap()), stringWriter, "post_message_rendere", new StringReader(text));
			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public String t(String tag, String code, Map<String, Object> args) {
		try {
			String text = translationService.getMessage(tag, "text", code, null, code, context.getCurrentLocale());
			StringWriter stringWriter = new StringWriter();
			velocityEngine.evaluate(new VelocityContext(args), stringWriter, "post_message_rendere", new StringReader(text));
			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

}
