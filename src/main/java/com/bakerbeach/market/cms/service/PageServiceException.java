package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.commons.ServiceException;

@SuppressWarnings("serial")
public class PageServiceException extends ServiceException {
	
	public static class PageNotFoundException extends PageServiceException{
		
	}

}
