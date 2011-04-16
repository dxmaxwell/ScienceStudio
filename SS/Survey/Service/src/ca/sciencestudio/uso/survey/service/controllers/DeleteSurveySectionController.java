package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;


public class DeleteSurveySectionController implements Controller {
	
	private SurveySectionDAO surveySectionDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveySectionIdStr = request.getParameter("id");
		
		int surveySectionId = 0;
		
		try {
			if(surveySectionIdStr != null) {
				surveySectionId = Integer.parseInt(surveySectionIdStr);
			}
		} catch(NumberFormatException e) {
			surveySectionId = 0;
		}
		
		if(surveySectionId > 0) {
			surveySectionDAO.removeSection(surveySectionId);
		}
		
		return null;
	}

	public void setSurveySectionDAO(SurveySectionDAO surveySectionDAO) {
		this.surveySectionDAO = surveySectionDAO;
	}

}
