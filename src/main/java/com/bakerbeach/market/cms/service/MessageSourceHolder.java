package com.bakerbeach.market.cms.service;

import org.springframework.context.MessageSource;

public class MessageSourceHolder {
	
	private static MessageSource messageSource;

	public static MessageSource getMessageSource() {
		return messageSource;
	}

	public static void setMessageSource(MessageSource messageSource) {
		MessageSourceHolder.messageSource = messageSource;
	}

}
