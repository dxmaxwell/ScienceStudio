package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;


public class SurveySectionFormController extends SimpleFormController {
	
	private SurveySectionDAO surveySectionDAO;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		SurveySection surveySection = (SurveySection) command;
		
		int surveySectionId = surveySection.getId();
		if (surveySectionId != 0) {
			surveySectionDAO.updateSection(surveySection);
		} else {
			surveySectionId = surveySectionDAO.addSection(surveySection);
		}
		
		int surveyId = surveySection.getSurveyId();
		return new ModelAndView("redirect:surveySection?surveySectionId=" + surveySectionId + "&surveyId=" + surveyId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String surveyIdParam = request.getParameter("surveyId");
		String surveySectionIdParam = request.getParameter("surveySectionId");
		
		int surveyId = 0;
		try {
			if (surveyIdParam != null) {
				surveyId = Integer.parseInt(surveyIdParam);
			}
		} catch (Exception e) {
			surveyId = 0;
		}

		int surveySectionId = 0;
		try {
			if (surveySectionIdParam != null) {
				surveySectionId = Integer.parseInt(surveySectionIdParam);
			}
		} catch (Exception e) {
			surveySectionId = 0;
		}
		
		SurveySection surveySection = null;
		
		if(surveySectionId != 0) {
			surveySection = surveySectionDAO.getSectionById(surveySectionId);
		} else {
			surveySection = new SurveySection();
			if (surveyId != 0) {
				surveySection.setSurveyId(surveyId);
			}
			surveySection.setOrder(surveySectionDAO.getMaxSectionOrder(surveyId));
		}
		
		return surveySection;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String param = request.getParameter("surveySectionId");
		int surveySectionId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if(param != null) {
				surveySectionId = Integer.parseInt(param);
			}
		} catch (Exception e) {
			surveySectionId = 0;
		}
		
		if(surveySectionId !=0) {
			model.put("titleMsg", "Editing survey section");
		} else {
			model.put("titleMsg", "New survey section");
		}
		
		return model;
	}

	public void setSurveySectionDAO(SurveySectionDAO surveySectionDAO) {
		this.surveySectionDAO = surveySectionDAO;
	}
	
}
