package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class GetSurveyCategoryController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyCategoryIdStr = request.getParameter("surveyCategoryId");
		String activeTab = request.getParameter("activeTab");
		
		int surveyCategoryId = 0;
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		try {
			if(surveyCategoryIdStr != null) {
				surveyCategoryId = Integer.parseInt(surveyCategoryIdStr);
			}
		} catch (NumberFormatException e) {
			surveyCategoryId = 0;
		}
		
		model.put("surveyCategoryId", surveyCategoryId);
		if (activeTab == null) {
			model.put("activeTab", "details");
		} else {
			model.put("activeTab", activeTab);
		}
		
		return new ModelAndView("surveyCategory", model);
	}

}
