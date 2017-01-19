package com.bakerbeach.market.cms.model;

public class Link {
	String href;
	String name;

	public Link(String href, String name){
		this.href = href;
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public String getName() {
		return name;
	}
}
