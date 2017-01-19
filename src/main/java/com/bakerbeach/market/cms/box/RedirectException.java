package com.bakerbeach.market.cms.box;

import com.bakerbeach.market.cms.model.Redirect;

@SuppressWarnings("serial")
public class RedirectException extends ProcessableBoxException{
	private Redirect redirect;
	
	public RedirectException(Redirect redirect){
		this.redirect = redirect;
	}
	
	public Redirect getRedirect(){
		return redirect;
	}
}
