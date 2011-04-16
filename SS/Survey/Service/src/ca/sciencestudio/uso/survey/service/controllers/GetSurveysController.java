package ca.sciencestudio.uso.survey.service.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.SurveyStatus;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.service.backers.SurveyListBacker;


public class GetSurveysController implements Controller {
	
	private SurveyDAO surveyDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyCategoryId = 0;
		List<Survey> surveys = null;
		List<SurveyListBacker> surveyListBackers = new ArrayList<SurveyListBacker>();
		
		//check passed parameter
		String objectId = request.getParameter("surveyCategoryId");
		try {
			if (objectId != null) {
				surveyCategoryId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyCategoryId = 0;
		}
		
		if (surveyCategoryId != 0) {
			surveys = surveyDAO.getSurveyListByCategory(surveyCategoryId);
			for (Survey survey : surveys) {
				SurveyListBacker backer = new SurveyListBacker();
				backer.setId(survey.getId());
				backer.setName(survey.getName());
				backer.setDescription(survey.getDescription());
				backer.setDeadline(survey.getDeadline());
				
				int status = survey.getStatus();
				SurveyStatus[] surveyStatusList = SurveyStatus.values();
				for (SurveyStatus surveyStatus : surveyStatusList) {
					if (surveyStatus.getValue() == status) {
						backer.setStatus(surveyStatus.getText());
						break;
					}
				}
				
				surveyListBackers.add(backer);
			}
		}
		
		model.put("surveys", surveyListBackers);
		model.put("surveyCategoryId", surveyCategoryId);
		
		return new ModelAndView("surveys", model);
	}
	
	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}
}
