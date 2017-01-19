package com.bakerbeach.market.cms.box;

import java.util.List;
import java.util.Set;

public interface Page {
	
	String getPageId();
	
	Box getBoxById(String id);
	
	Box getRootBox();
	
	Set<String> getBoxIds();
	
	List<Box> getBoxesByType(String type);
	
	void addBox(Box box);

}
