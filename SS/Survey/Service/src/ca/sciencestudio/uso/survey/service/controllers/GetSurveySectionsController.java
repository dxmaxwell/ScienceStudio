package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;


public class GetSurveySectionsController implements Controller {
	
	private SurveySectionDAO surveySectionDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyId = 0;
		List<SurveySection> surveySections = null;
		
		//check passed parameter
		String objectId = request.getParameter("surveyId");
		try {
			if (objectId != null) {
				surveyId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyId = 0;
		}
		
		if (surveyId != 0)
			surveySections = surveySectionDAO.getSectionList(surveyId);
		
		model.put("surveySections", surveySections);
		model.put("surveyId", surveyId);
		
		return new ModelAndView("surveySections", model);
	}
	
	public void setSurveySectionDAO(SurveySectionDAO surveySectionDAO) {
		this.surveySectionDAO = surveySectionDAO;
	}
}
