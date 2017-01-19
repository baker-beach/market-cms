package com.bakerbeach.market.cms.service;

@SuppressWarnings("serial")
public class CmsContextException extends Exception {
	
	public CmsContextException(String msg){
		super(msg);
	}
	
	public CmsContextException(Exception exception){
		super(exception);
	}

}
