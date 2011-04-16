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
import ca.sciencestudio.uso.survey.model.SurveyStatistic;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.model.dao.SurveyStatisticDAO;
import ca.sciencestudio.uso.survey.service.backers.SurveyStatisticBacker;
import ca.sciencestudio.uso.survey.service.backers.SurveyStatisticDetailBacker;


public class GetSurveyStatisticsController implements Controller {
	
	private SurveyDAO surveyDAO;
	private SurveyStatisticDAO surveyStatisticDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyId = 0;
		SurveyStatisticBacker surveyStatisticBacker = new SurveyStatisticBacker();
		
		//check passed parameter
		String objectId = request.getParameter("surveyId");
		try {
			if (objectId != null) {
				surveyId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyId = 0;
		}
		
		if (surveyId != 0) {
			Survey survey = surveyDAO.getSurveyById(surveyId);
			surveyStatisticBacker.setName(survey.getName());
			surveyStatisticBacker.setDeadline(survey.getDeadline());
			surveyStatisticBacker.setDescription(survey.getDescription());
			surveyStatisticBacker.setStatus(survey.getStatus());
			
			SurveyStatistic statistic = surveyStatisticDAO.getExpectedParticipantCount(surveyId);
			int expectedParticipantCount = statistic.getCount();
			surveyStatisticBacker.setExpectedParticipantCount(expectedParticipantCount);
			
			statistic = surveyStatisticDAO.getAttendedParticipantCount(surveyId);
			int attendedParticipantCount = statistic.getCount();
			surveyStatisticBacker.setAttendedParticipantCount(attendedParticipantCount);
			
			surveyStatisticBacker.setParticipateRate((expectedParticipantCount == 0)?0:(double) attendedParticipantCount / expectedParticipantCount);
			
			List<SurveyStatisticDetailBacker> statisticList = new ArrayList<SurveyStatisticDetailBacker>();
			SurveyStatisticDetailBacker surveyStatisticDetailBacker;
			
			int sectionIndex = 1, questionIndex = 1;
			int answerCount;
			//get all sections
			List<SurveyStatistic> sectionList = surveyStatisticDAO.getSectionList(surveyId);
			//loop all sections to add them and their questions to statistic list
			for (SurveyStatistic section : sectionList) {
				int sectionId = section.getSectionId();
				
				surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
				
				surveyStatisticDetailBacker.setNumberer(sectionIndex ++);
				
				List<SurveyStatistic> questionList = surveyStatisticDAO.getQuestionList(sectionId);
				surveyStatisticDetailBacker.setCount(questionList.size());
				
				surveyStatisticDetailBacker.setType(section.getType());
				surveyStatisticDetailBacker.setSectionId(section.getSectionId());
				surveyStatisticDetailBacker.setQuestionId(section.getQuestionId());
				surveyStatisticDetailBacker.setChoiceId(section.getChoiceId());
				surveyStatisticDetailBacker.setText(section.getText());
				surveyStatisticDetailBacker.setPercentage(section.getPercentage());
				
				statisticList.add(surveyStatisticDetailBacker);
				
				//loop all question in this section to add them to statistic list
				for (SurveyStatistic question : questionList) {
					int questionId = question.getQuestionId();
					int questionType = question.getType();
					
					surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
					
					surveyStatisticDetailBacker.setNumberer(questionIndex ++);
					
					answerCount = question.getCount();
					surveyStatisticDetailBacker.setCount(answerCount);
					surveyStatisticDetailBacker.setPercentage((attendedParticipantCount == 0)?0:(double) answerCount / attendedParticipantCount);
					
					surveyStatisticDetailBacker.setSectionId(sectionId);
					surveyStatisticDetailBacker.setType(questionType);
					
					surveyStatisticDetailBacker.setQuestionId(question.getQuestionId());
					surveyStatisticDetailBacker.setChoiceId(question.getChoiceId());
					surveyStatisticDetailBacker.setText(question.getText());
					
					statisticList.add(surveyStatisticDetailBacker);
					
					//get choices or answers accordingly to question's type
					List<SurveyStatistic> allAnswerList = new ArrayList<SurveyStatistic>();
					if (questionType == 4) { //PlainText question
						//get all plain answers to this question
						allAnswerList = surveyStatisticDAO.getAllAnswerList(questionId);
						for (int i = 0; i < allAnswerList.size(); i ++) {
							if (i < 5) { //Only show first 5 answers to this question
								surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
								
								surveyStatisticDetailBacker.setNumberer(i + 1);
								surveyStatisticDetailBacker.setType(allAnswerList.get(i).getType());
								surveyStatisticDetailBacker.setSectionId(sectionId);
								surveyStatisticDetailBacker.setQuestionId(questionId);
								surveyStatisticDetailBacker.setChoiceId(allAnswerList.get(i).getChoiceId());
								surveyStatisticDetailBacker.setText(allAnswerList.get(i).getText());
								surveyStatisticDetailBacker.setCount(allAnswerList.get(i).getCount());
								surveyStatisticDetailBacker.setPercentage((answerCount==0)?0:(double) allAnswerList.get(i).getCount() / answerCount);
								
								statisticList.add(surveyStatisticDetailBacker);
							} else {
								allAnswerList.get(i).setNumberer(i + 1);
								allAnswerList.get(i).setSectionId(sectionId);
								allAnswerList.get(i).setQuestionId(questionId);
								allAnswerList.get(i).setPercentage((answerCount==0)?0:(double) allAnswerList.get(i).getCount() / answerCount);
							}
						}
						//replacing the rest with 'more answers'
						if (allAnswerList.size() > 5) {
							surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
							
							surveyStatisticDetailBacker.setType(8);
							surveyStatisticDetailBacker.setSectionId(sectionId);
							surveyStatisticDetailBacker.setQuestionId(questionId);
							surveyStatisticDetailBacker.setText("more answers...");
							
							surveyStatisticDetailBacker.setStatisticDetailList(allAnswerList.subList(5, allAnswerList.size() - 1));
							
							statisticList.add(surveyStatisticDetailBacker);
						}
					} else { //Multi-Choices or multi-Choices+OtherAnswer
						//get all choices to this question
						allAnswerList = surveyStatisticDAO.getChoiceList(questionId);
						for (int i = 0; i < allAnswerList.size(); i ++) {
							surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
							
							surveyStatisticDetailBacker.setNumberer(i + 1);
							surveyStatisticDetailBacker.setType(allAnswerList.get(i).getType());
							surveyStatisticDetailBacker.setSectionId(sectionId);
							surveyStatisticDetailBacker.setQuestionId(questionId);
							surveyStatisticDetailBacker.setChoiceId(allAnswerList.get(i).getChoiceId());
							surveyStatisticDetailBacker.setText(allAnswerList.get(i).getText());
							surveyStatisticDetailBacker.setCount(allAnswerList.get(i).getCount());
							surveyStatisticDetailBacker.setPercentage((answerCount==0)?0:(double) allAnswerList.get(i).getCount() / answerCount);
							
							statisticList.add(surveyStatisticDetailBacker);
						}
						
						if (questionType == 3) { //Multi-Choices+OtherAnswer
							//add one more choice 'Other' to this question
							List<SurveyStatistic> otherAnswers = surveyStatisticDAO.getAllOtherAnswerList(questionId);
							
							surveyStatisticDetailBacker = new SurveyStatisticDetailBacker();
							
							surveyStatisticDetailBacker.setNumberer(allAnswerList.size() + 1);
							surveyStatisticDetailBacker.setType(6);
							surveyStatisticDetailBacker.setSectionId(sectionId);
							surveyStatisticDetailBacker.setQuestionId(questionId);
							surveyStatisticDetailBacker.setChoiceId(0);
							surveyStatisticDetailBacker.setText("Other");
							surveyStatisticDetailBacker.setCount(otherAnswers.size());
							surveyStatisticDetailBacker.setPercentage((answerCount==0)?0:(double) otherAnswers.size() / answerCount);
							
							if (otherAnswers.size() > 0) {
								for (int i = 0; i < otherAnswers.size(); i++) {
									otherAnswers.get(i).setPercentage(otherAnswers.get(i).getCount() / answerCount);
								}
								surveyStatisticDetailBacker.setStatisticDetailList(otherAnswers);
							}
							
							statisticList.add(surveyStatisticDetailBacker);
						}
					}
					
				}
				statisticList.add(new SurveyStatisticDetailBacker()); //empty line to separate questions
			}
			
			surveyStatisticBacker.setStatisticList(statisticList);
		}
		
		model.put("surveyStatistic", surveyStatisticBacker);
		model.put("surveyId", surveyId);
		
		return new ModelAndView("surveyStatistics", model);
	}
	
	public void setSurveyStatisticDAO(SurveyStatisticDAO surveyStatisticDAO) {
		this.surveyStatisticDAO = surveyStatisticDAO;
	}
	
	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}
}
