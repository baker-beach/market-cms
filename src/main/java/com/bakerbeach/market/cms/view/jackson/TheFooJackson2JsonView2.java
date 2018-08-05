package com.bakerbeach.market.cms.view.jackson;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TheFooJackson2JsonView2 extends MarketJackson2JsonView {
	public static final String DEFAULT_CONTENT_TYPE = "application/json";

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		super.render(model, request, response);
	}

}
