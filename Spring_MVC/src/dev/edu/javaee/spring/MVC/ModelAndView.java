package dev.edu.javaee.spring.MVC;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
	private Map<String, String> request_Map = new HashMap<String, String>();
	private Map<String, String> response_Map = new HashMap<String, String>();
	private String viewName;

	public void putRequest_Map(String name, String value) {
		request_Map.put(name, value);
	}

	public String getRequest_Map(String key) {
		return request_Map.get(key);
	}


	public Map<String, String> getResponse_Map() {
		return response_Map;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void addObject(String name, String value) {
		response_Map.put(name, value);
	}
	
}
