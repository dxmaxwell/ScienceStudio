package ca.sciencestudio.uso.survey.service.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.model.SurveyCategory;
import ca.sciencestudio.uso.survey.model.dao.SurveyCategoryDAO;
import ca.sciencestudio.uso.survey.model.SurveyStatus;


public class SurveyFormController extends SimpleFormController {
	
	private SurveyDAO surveyDAO;
	private SurveyCategoryDAO surveyCategoryDAO;
	private int surveyCategoryId = 0;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		Survey survey = (Survey) command;
		
		int surveyId = survey.getId();
		if (surveyId != 0) {
			surveyDAO.updateSurvey(survey);
		} else {
			surveyId = surveyDAO.addSurvey(survey);
		}
		
		return new ModelAndView("redirect:survey?surveyId=" + surveyId + "&surveyCategoryId=" + surveyCategoryId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String surveyIdParam = request.getParameter("surveyId");
		String surveyCategoryIdParam = request.getParameter("surveyCategoryId");
		
		int surveyId = 0;

		try {
			if (surveyIdParam != null) {
				surveyId = Integer.parseInt(surveyIdParam);
			}
		} catch (Exception e) {
			surveyId = 0;
		}

		try {
			if (surveyCategoryIdParam != null) {
				surveyCategoryId = Integer.parseInt(surveyCategoryIdParam);
			}
		} catch (Exception e) {
			surveyCategoryId = 0;
		}
		
		Survey survey = null;
		
		if (surveyId != 0) {
			survey = surveyDAO.getSurveyById(surveyId);
		} else {
			survey = new Survey();
			if (surveyCategoryId != 0) {
				survey.setCategoryId(surveyCategoryId);
			}
			survey.setStatus(0);
		}
		
		return survey;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String param = request.getParameter("surveyId");
		int surveyId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if(param != null) {
				surveyId = Integer.parseInt(param);
			}
		} catch (Exception e) {
			surveyId = 0;
		}
		
		if (surveyId !=0) {
			model.put("titleMsg", "Editing survey");
		} else {
			model.put("titleMsg", "New survey");
		}
		
		List<SurveyCategory> surveyCategories = surveyCategoryDAO.getSurveyCategoryList();
		model.put("surveyCategories", surveyCategories);
		
		List<SurveyStatus> surveyStatusList = Arrays.asList(SurveyStatus.values());
		model.put("surveyStatusList", surveyStatusList);
		
		return model;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}
	
	public void setSurveyCategoryDAO(SurveyCategoryDAO surveyCategoryDAO) {
		this.surveyCategoryDAO = surveyCategoryDAO;
	}
	
}
