package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyCategory;
import ca.sciencestudio.uso.survey.model.dao.SurveyCategoryDAO;


public class GetSurveyCategoriesController implements Controller {
	
	private SurveyCategoryDAO surveyCategoryDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		List<SurveyCategory> surveyCategories = surveyCategoryDAO.getSurveyCategoryList();
		
		model.put("surveyCategories", surveyCategories);
		
		return new ModelAndView("surveyCategories", model);
	}
	
	public void setSurveyCategoryDAO(SurveyCategoryDAO surveyCategoryDAO) {
		this.surveyCategoryDAO = surveyCategoryDAO;
	}
}
