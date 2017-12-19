package com.bakerbeach.market.cms.box;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface Box {

	String getId();

	void setId(String id);

	String getType();

	void setType(String type);

	String getTemplate();

	void setTemplate(String template);

	Map<String, Object> getData();

	void setData(Map<String, Object> data);

	void setMetaData(Map<String, Object> data);

	Map<String, List<Box>> getContainerMap();

	List<Box> getChildBoxesByContainer(String region);

	void addChildBox(String region, Box box);

	void handleRenderRequest(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap);
	
	Date getStartDate();
	
	void setStartDate(Date date);
	
	void setStartDate(String date);
	
	Date getEndDate();
	
	void setEndDate(Date date);	
	
	void setEndDate(String date);	
	
	boolean isActive();

}
