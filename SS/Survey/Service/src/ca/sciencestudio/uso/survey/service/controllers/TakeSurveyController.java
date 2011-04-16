package ca.sciencestudio.uso.survey.service.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsecurity.SecurityUtils;
import org.jsecurity.subject.Subject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import ca.lightsource.directory.model.Person;
import ca.lightsource.directory.model.dao.PersonDAO;
import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;
import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;
import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;
import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;
import ca.sciencestudio.uso.survey.model.SurveyFeedback;
import ca.sciencestudio.uso.survey.model.dao.SurveyFeedbackDAO;
import ca.sciencestudio.uso.survey.service.backers.SurveyBacker;
import ca.sciencestudio.uso.survey.service.backers.SurveySectionBacker;
import ca.sciencestudio.uso.survey.service.backers.SurveyQuestionBacker;
import ca.sciencestudio.uso.survey.service.backers.SurveyChoiceBacker;


public class TakeSurveyController extends SimpleFormController {
	
	private PersonDAO personDAO;
	private SurveyDAO surveyDAO;
	private SurveyParticipantDAO surveyParticipantDAO;
	private SurveySectionDAO surveySectionDAO;
	private SurveyQuestionDAO surveyQuestionDAO;
	private SurveyChoiceDAO surveyChoiceDAO;
	private SurveyFeedbackDAO surveyFeedbackDAO;
	private String directoryPath;
	
	private int surveyId = -1;
	private int participantId = 0;
	private SurveyBacker surveyBacker = new SurveyBacker();
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int noAnswerQuestionCount = 0;
		
		SurveyBacker backer = (SurveyBacker) command;
		List<SurveySectionBacker> sections = backer.getSectionBackers();
		for (SurveySectionBacker section : sections) {
			List<SurveyQuestionBacker> questions = section.getQuestionBackers();
			for (SurveyQuestionBacker question : questions) {
				if ((question.getType() <= 1) && (question.getChoice() == -1)) {
					//No choice to multi-choices(+OtherAnswer) question 
					noAnswerQuestionCount ++;
					continue;
				}
				if (question.getType() == 2 && 
					(question.getAnswer() == null || question.getAnswer().trim().isEmpty())
					) { //No answer to plain text question
					noAnswerQuestionCount ++;
					SurveyFeedback feedback = surveyFeedbackDAO.getFeedback(question.getId(), participantId);
					if (feedback != null) { //if there was one answer, remove it
						surveyFeedbackDAO.removeFeedback(feedback.getId());
					}
					continue;
				}
				
				SurveyFeedback feedback = surveyFeedbackDAO.getFeedback(question.getId(), participantId);
				if (feedback == null) {
					feedback = new SurveyFeedback();
					feedback.setQuestionId(question.getId());
					feedback.setParticipantId(participantId);
					feedback.setChoice(question.getChoice());
					if (question.getAnswer() == null) {
						feedback.setAnswer(null);
					} else {
						feedback.setAnswer(question.getAnswer().trim());
					}
					surveyFeedbackDAO.addFeedback(feedback);
				} else {
					feedback.setChoice(question.getChoice());
					//clean answer if neither is Other chosen nor is PlainText type question
					if ((question.getType() == 1 && question.getChoice() == 0) || question.getType() == 2) {
						feedback.setAnswer(question.getAnswer().trim());
					} else {
						feedback.setAnswer("");
					}
					surveyFeedbackDAO.updateFeedback(feedback);
				}
			}
		}
		
		//Update the status of the participant accordingly
		SurveyParticipant participant = surveyParticipantDAO.getParticipantById(participantId);
		int status = 0;
		if (WebUtils.hasSubmitParameter(request, "save")) { //'Started' if Save button is clicked
			if (noAnswerQuestionCount == backer.getQuestionCount()) { //'Created' if no question answered
				status = 0 ;
			} else status = 1;
		} else { //'Submitted' else
			status = 2;
		}
		if (participant.getStatus() != status) { //only update when needed
			participant.setStatus(status);
			surveyParticipantDAO.updateParticipant(participant);
		}
		
		//Update the status of survey as Started
		if (status > 0) {
			Survey survey = surveyDAO.getSurveyById(surveyId);
			if (survey != null) {
				survey.setStatus(1);
				surveyDAO.updateSurvey(survey);
			}
		}
		
		model.put("survey", backer);
		model.put("noAnswerQuestionCount", noAnswerQuestionCount);
		model.put("status", status==2?"submitted":"saved");
		
		return new ModelAndView("takeSurveyFeedback", model);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		String surveyIdParam = request.getParameter("surveyId");
		String participantIdParam = request.getParameter("participantId");
		
		if (surveyIdParam == null) {
			surveyBacker.setStatus(-1);
			return surveyBacker;
		}
		
		try {
			surveyId = Integer.parseInt(surveyIdParam);
		} catch (Exception e) {
			surveyBacker.setStatus(3);
			return surveyBacker;
		}
		
		Survey survey = surveyDAO.getSurveyById(surveyId);
		if (survey == null) { //surveyId invalid
			surveyBacker.setStatus(3);
			return surveyBacker;
		}
		
		if (participantIdParam != null) {
			try {
				participantId = Integer.parseInt(participantIdParam);
			} catch (Exception e) {
				participantId = 0;
			}
		}
		
		int questionCount = 0;
		
		surveyBacker = new SurveyBacker();
		surveyBacker.setId(survey.getId());
		surveyBacker.setName(survey.getName());
		surveyBacker.setDescription(survey.getDescription());
		surveyBacker.setParticipantId(participantId);
		surveyBacker.setStatus(survey.getStatus());
		surveyBacker.setDeadline(survey.getDeadline());
		
		if (participantId == 0) {
			surveyBacker.setStatus(1);
		} else {
			// Check the current login user is the real person this participant refers to
			SurveyParticipant surveyParticipant = surveyParticipantDAO.getParticipantById(participantId);
			if (surveyParticipant == null) {
				surveyBacker.setStatus(4);
				return surveyBacker;
			}
			
			Subject subject = SecurityUtils.getSubject();
			String username = subject.getPrincipal().toString();
			Person person = personDAO.getPersonByDirectoryKey(username);
			if (person == null) {
				surveyBacker.setStatus(4);
				return surveyBacker;
			}
			
			if (person.getId() != surveyParticipant.getPersonId()) {
				surveyBacker.setStatus(4);
				return surveyBacker;
			}
			
			if (surveyParticipant.getStatus() == 2) { //Submitted
				surveyBacker.setStatus(5);
				return surveyBacker;
			}
			
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			
			Calendar cDeadline = Calendar.getInstance();
			cDeadline.setTime(survey.getDeadline());
			if (today.after(cDeadline)) {//expired
				surveyBacker.setStatus(6);
				return surveyBacker;
			}
		}
		
		List<SurveySection> surveySections = surveySectionDAO.getSectionList(surveyId);
		for (SurveySection surveySection : surveySections) {
			SurveySectionBacker sectionBacker = new SurveySectionBacker();
			sectionBacker.setId(surveySection.getId());
			sectionBacker.setName(surveySection.getName());
			sectionBacker.setDescription(surveySection.getDescription());
			
			List<SurveyQuestion> surveyQuestions = surveyQuestionDAO.getQuestionListBySectionId(surveySection.getId());
			for (SurveyQuestion surveyQuestion : surveyQuestions) {
				SurveyQuestionBacker questionBacker = new SurveyQuestionBacker();
				questionBacker.setId(surveyQuestion.getId());
				questionBacker.setText(surveyQuestion.getText());
				questionBacker.setType(surveyQuestion.getType());
				if (surveyQuestion.getAnswerLength() == 0) {
					questionBacker.setAnswerLength(2000);
				} else {
					questionBacker.setAnswerLength(surveyQuestion.getAnswerLength());
				}
				String questionImage = surveyQuestion.getImage();
				if (questionImage != null && !questionImage.equals("")) {
					questionBacker.setImage(directoryPath + questionImage);
				}
				
				List<SurveyChoice> surveyChoices = surveyChoiceDAO.getChoiceList(surveyQuestion.getId());
				for (SurveyChoice surveyChoice : surveyChoices) {
					SurveyChoiceBacker choiceBacker = new SurveyChoiceBacker();
					choiceBacker.setId(surveyChoice.getId());
					choiceBacker.setText(surveyChoice.getText());
					String choiceImage = surveyChoice.getImage();
					if (choiceImage != null && !choiceImage.equals("")) {
						choiceBacker.setImage(directoryPath + choiceImage);
					}
					
					questionBacker.getChoiceBackers().add(choiceBacker);
				}
				
				if (participantId == 0) {
					questionBacker.setChoice(-1);
				} else {
					SurveyFeedback surveyFeedback = surveyFeedbackDAO.getFeedback(surveyQuestion.getId(), participantId);
					if (surveyFeedback == null) {
						questionBacker.setChoice(-1);
					} else {
						questionBacker.setChoice(surveyFeedback.getChoice());
						if ((surveyQuestion.getType() == 1 && surveyFeedback.getChoice() == 0) || surveyQuestion.getType() == 2) {
							questionBacker.setAnswer(surveyFeedback.getAnswer());
						}
					}
				}
				
				sectionBacker.getQuestionBackers().add(questionBacker);
			}
			sectionBacker.setQuestionCount(surveyQuestions.size());
			surveyBacker.getSectionBackers().add(sectionBacker);
			
			questionCount += surveyQuestions.size();
		}
		
		surveyBacker.setSectionCount(surveySections.size());
		surveyBacker.setQuestionCount(questionCount);
		
		return surveyBacker;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		return null;
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}

	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}

	public void setSurveySectionDAO(SurveySectionDAO surveySectionDAO) {
		this.surveySectionDAO = surveySectionDAO;
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}

	public void setSurveyFeedbackDAO(SurveyFeedbackDAO surveyFeedbackDAO) {
		this.surveyFeedbackDAO = surveyFeedbackDAO;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
}
