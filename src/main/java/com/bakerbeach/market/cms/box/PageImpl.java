package com.bakerbeach.market.cms.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;

public class PageImpl implements Page {

	Map<String, List<Box>> boxesByType = new HashMap<>();
	Map<String, Box> boxesById = new HashMap<>();
	String pageId;
	Box rootBox;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public Box getRootBox() {
		return rootBox;
	}

	public Set<String> getBoxIds() {
		return boxesById.keySet();
	}

	public void setRootBox(Box rootBox) {
		this.rootBox = rootBox;
	}

	public Box getBoxById(String id) {
		return boxesById.get(id);
	}

	public void addBox(Box box) {
		if (boxesByType.containsKey(box.getType()))
			boxesByType.get(box.getType()).add(box);
		else {
			List<Box> boxes = new ArrayList<>();
			boxes.add(box);
			boxesByType.put(box.getType(), boxes);
		}
		boxesById.put(box.getId(), box);
	}

	public Integer getBoxCountByType(String boxType) {
		if (boxesByType.containsKey(boxType))
			return boxesByType.get(boxType).size();
		else
			return 0;
	}

	@Override
	public List<Box> getBoxesByType(String type) {
		return boxesByType.get(type);
	}

}
