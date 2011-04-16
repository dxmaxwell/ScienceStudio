package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveyCategory;
import ca.sciencestudio.uso.survey.model.dao.SurveyCategoryDAO;


public class SurveyCategoryFormController extends SimpleFormController {
	
	private SurveyCategoryDAO surveyCategoryDAO;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		SurveyCategory surveyCategory = (SurveyCategory) command;
		
		int surveyCategoryId = surveyCategory.getId();
		if (surveyCategoryId != 0) {
			surveyCategoryDAO.updateSurveyCategory(surveyCategory);
		} else {
			surveyCategoryId = surveyCategoryDAO.addSurveyCategory(surveyCategory);
		}
		
		return new ModelAndView("redirect:surveyCategory?surveyCategoryId=" + surveyCategoryId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String param = request.getParameter("surveyCategoryId");
		int surveyCategoryId = 0;

		try {
			if (param != null) {
				surveyCategoryId = Integer.parseInt(param);
			}
		} catch (Exception e) {
			surveyCategoryId = 0;
		}
		
		SurveyCategory surveyCategory = null;
		
		if (surveyCategoryId != 0) {
			surveyCategory = surveyCategoryDAO.getSurveyCategoryById(surveyCategoryId);
		} else {
			surveyCategory = new SurveyCategory();
		}
		
		return surveyCategory;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String param = request.getParameter("surveyCategoryId");
		int surveyCategoryId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if (param != null) {
				surveyCategoryId = Integer.parseInt(param);
			}
		} catch (Exception e) {
			surveyCategoryId = 0;
		}
		
		if (surveyCategoryId !=0) {
			model.put("titleMsg", "Editing survey category");
		} else {
			model.put("titleMsg", "New survey category");
		}
		
		return model;
	}

	public void setSurveyCategoryDAO(SurveyCategoryDAO surveyCategoryDAO) {
		this.surveyCategoryDAO = surveyCategoryDAO;
	}
}
