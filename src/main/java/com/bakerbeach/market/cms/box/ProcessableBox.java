package com.bakerbeach.market.cms.box;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface ProcessableBox {
	
	void handleActionRequest(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws ProcessableBoxException;
}
