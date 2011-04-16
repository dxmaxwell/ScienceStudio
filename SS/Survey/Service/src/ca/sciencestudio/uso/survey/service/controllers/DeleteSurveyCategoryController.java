package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyCategoryDAO;


public class DeleteSurveyCategoryController implements Controller {
	
	private SurveyCategoryDAO surveyCategoryDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyCategoryIdStr = request.getParameter("id");
		
		int surveyCategoryId = 0;
		
		try {
			if (surveyCategoryIdStr != null) {
				surveyCategoryId = Integer.parseInt(surveyCategoryIdStr);
			}
		} catch (NumberFormatException e) {
			surveyCategoryId = 0;
		}
		
		if (surveyCategoryId > 0) {
			surveyCategoryDAO.removeSurveyCategory(surveyCategoryId);
		}
		
		return null;
	}

	public void setSurveyCategoryDAO(SurveyCategoryDAO surveyCategoryDAO) {
		this.surveyCategoryDAO = surveyCategoryDAO;
	}

}
