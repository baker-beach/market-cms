package com.bakerbeach.market.cms.model;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Breadcrumbs extends ArrayList<Link>{

	public void addLink(String href, String name){
		this.add(new Link(href, name));
	}

}
