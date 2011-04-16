package ca.sciencestudio.uso.survey.service.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.SurveyQuestionType;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;
import ca.sciencestudio.uso.survey.service.backers.SurveyQuestionListBacker;


public class SearchSurveyQuestionsController implements Controller {
	
	private SurveyQuestionDAO surveyQuestionDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		List<SurveyQuestion> surveyQuestions = null;
		List<SurveyQuestionListBacker> surveyQuestionListBackers = new ArrayList<SurveyQuestionListBacker>();
		
		surveyQuestions = surveyQuestionDAO.getAllQuestionList();
		for (SurveyQuestion surveyQuestion : surveyQuestions) {
			SurveyQuestionListBacker backer = new SurveyQuestionListBacker();
			backer.setId(surveyQuestion.getId());
			backer.setText(surveyQuestion.getText());
			
			int type = surveyQuestion.getType();
			SurveyQuestionType[] surveyQuestionTypes = SurveyQuestionType.values();
			for (SurveyQuestionType surveyQuestionType : surveyQuestionTypes) {
				if (surveyQuestionType.getValue() == type) {
					backer.setType(surveyQuestionType.getText());
					break;
				}
			}

			backer.setImage(surveyQuestion.getImage());
			
			surveyQuestionListBackers.add(backer);
		}
		
		model.put("surveyQuestions", surveyQuestionListBackers);
		
		return new ModelAndView("surveyQuestionPick", model);
	}
	
	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}
}
