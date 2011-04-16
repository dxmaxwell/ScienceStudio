package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;


public class SurveyChoiceFormController extends SimpleFormController {
	
	private SurveyChoiceDAO surveyChoiceDAO;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		SurveyChoice surveyChoice = (SurveyChoice) command;
		
		int surveyChoiceId = surveyChoice.getId();
		if (surveyChoiceId != 0) {
			surveyChoiceDAO.updateChoice(surveyChoice);
		} else {
			surveyChoiceId = surveyChoiceDAO.addChoice(surveyChoice);
		}
		
		int surveyQuestionId = surveyChoice.getQuestionId();
		return new ModelAndView("redirect:surveyChoice?surveyChoiceId=" + surveyChoiceId + "&surveyQuestionId=" + surveyQuestionId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String surveyQuestionIdParam = request.getParameter("surveyQuestionId");
		String surveyChoiceIdParam = request.getParameter("surveyChoiceId");
		
		int surveyQuestionId = 0;
		try {
			if (surveyQuestionIdParam != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdParam);
			}
		} catch (Exception e) {
			surveyQuestionId = 0;
		}
		
		int surveyChoiceId = 0;
		try {
			if (surveyChoiceIdParam != null) {
				surveyChoiceId = Integer.parseInt(surveyChoiceIdParam);
			}
		} catch (Exception e) {
			surveyChoiceId = 0;
		}
		
		SurveyChoice surveyChoice = null;
		
		if(surveyChoiceId != 0) {
			surveyChoice = surveyChoiceDAO.getChoiceById(surveyChoiceId);
		} else {
			surveyChoice = new SurveyChoice();
			if (surveyQuestionId != 0) {
				surveyChoice.setQuestionId(surveyQuestionId);
			}
			surveyChoice.setOrder(surveyChoiceDAO.getMaxChoiceOrder(surveyQuestionId));
		}
		
		return surveyChoice;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String surveyQuestionIdParam = request.getParameter("surveyQuestionId");
		String surveyChoiceIdParam = request.getParameter("surveyChoiceId");
		int surveyQuestionId = 0;
		int surveyChoiceId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if(surveyQuestionIdParam != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdParam);
			}
		} catch (Exception e) {
			surveyQuestionId = 0;
		}
		
		try {
			if(surveyChoiceIdParam != null) {
				surveyChoiceId = Integer.parseInt(surveyChoiceIdParam);
			}
		} catch (Exception e) {
			surveyChoiceId = 0;
		}
		
		if (surveyChoiceId !=0) {
			model.put("titleMsg", "Editing survey choice");
		} else {
			model.put("titleMsg", "New survey choice");
		}
		
		model.put("surveyQuestionId", surveyQuestionId);
		
		return model;
	}

	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}
}
