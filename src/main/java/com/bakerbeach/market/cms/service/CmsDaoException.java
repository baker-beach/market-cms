package com.bakerbeach.market.cms.service;

@SuppressWarnings("serial")
public class CmsDaoException extends Exception {
	
	public CmsDaoException(String msg){
		super(msg);
	}
	
	public static class StructureNotFoundException extends CmsDaoException{
		
		public StructureNotFoundException(String msg){
			super(msg);
		}
		
	}

}
