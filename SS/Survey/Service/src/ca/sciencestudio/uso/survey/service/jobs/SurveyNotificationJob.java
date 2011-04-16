package ca.sciencestudio.uso.survey.service.jobs;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import ca.lightsource.directory.model.Person;
import ca.lightsource.directory.model.dao.PersonDAO;
import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipantNotification;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantNotificationDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipantNotifyEvent;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantNotifyEventDAO;


public class SurveyNotificationJob extends QuartzJobBean {
	private SurveyParticipantNotificationDAO surveyParticipantNotificationDAO;
	private SurveyDAO surveyDAO;
	private PersonDAO personDAO;
	private SurveyParticipantDAO surveyParticipantDAO;
	private SurveyParticipantNotifyEventDAO surveyParticipantNotifyEventDAO;
    private JavaMailSenderImpl mailSender;
    private String linkPrefix;
    
    private static final Logger logger = Logger.getLogger(SurveyNotificationJob.class);

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		SimpleDateFormat format = new SimpleDateFormat("MMMMM d, yyyy");
		
		logger.info("Starting Survey Notification...");
		
		List<SurveyParticipantNotification> notifications = surveyParticipantNotificationDAO.getNotificationList();
		
		for (SurveyParticipantNotification notification : notifications) {
			int participantId = notification.getParticipantId();
			if (participantId == 0) continue;
			
			int notificationType = notification.getNotificationType();
			
			SurveyParticipant surveyParticipant = surveyParticipantDAO.getParticipantById(participantId);
			if (surveyParticipant == null) continue;
			
			int personId = surveyParticipant.getPersonId();
			int surveyId = surveyParticipant.getSurveyId();
			
			Survey survey = surveyDAO.getSurveyById(surveyId);
			if (survey == null) continue;
			
			Person person = personDAO.getPersonById(personId);
			if (person == null) continue;
			
			String surveyName = survey.getName();
			String firstName = person.getFirstName();
			String lastName = person.getLastName();
			String beamLine = surveyParticipant.getBeamline();
			if (beamLine == null) {
				beamLine = "";
			} else {
				beamLine = ", beamline: " + beamLine;
			}
			
			String recipient = person.getEmailAddress();
			String sender = "survey.cls@gmail.com";
			
			
			String subject = "";
			switch (notificationType) {
				case 0:
					subject = "Notification"; break;
				case 1:
					subject = "First Reminder"; break;
				case 2:
					subject = "Second Reminder"; break;
			}
			subject = subject + " of survey " + surveyName + " for " + lastName + ", " + firstName + beamLine;
			
			String content = "<html><body>This is the ";
			switch (notificationType) {
				case 0:
					content = content + "notification"; break;
				case 1:
					content = content + "first reminder"; break;
				case 2:
					content = content + "second reminder"; break;		
			}
			content = content + " of survey <I>" + surveyName + " for " + lastName + ", " + firstName + beamLine + "</I>"
				+ ". The deadline is " + format.format(survey.getDeadline()) + ".<br>"
				+ "Please click the below link to take the survey: <br>"
				+ "<a href='" + linkPrefix + "/uso/survey/takeSurvey?surveyId=" + surveyId + "&participantId=" + participantId
				+ "'>" + linkPrefix + "/uso/survey/takeSurvey?surveyId=" + surveyId + "&participantId=" + participantId + "</a><br><br>"
				+ "NOTE: Please do NOT reply this email.<br><br>"
				+ "Sincerely,<br><br>"
				+ "CLS User Office"
				+ "</body></html>";
			
			try {
				final MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				
				helper.setTo(recipient);
				helper.setFrom(sender);
				helper.setSubject(subject);
				helper.setText(content, true);
				
				mailSender.send(message);
				
				SurveyParticipantNotifyEvent surveyParticipantNotifyEvent = new SurveyParticipantNotifyEvent();
				surveyParticipantNotifyEvent.setParticipantId(participantId);
				surveyParticipantNotifyEvent.setPersonId(personId);
				String event = "Notified";
				switch (notificationType) {
					case 0:
						event = "Notified"; break;
					case 1:
						event = "Reminded First Time"; break;
					case 2:
						event = "Reminded Second Time"; break;		
				}
				surveyParticipantNotifyEvent.setEvent(event);
				surveyParticipantNotifyEventDAO.addParticipantNotifyEvent(surveyParticipantNotifyEvent);
				
				logger.info("Email has been sent to person: " + lastName + ", " + firstName + "<" + recipient +"> for survey " + surveyName + beamLine);
			}
			catch (Exception e) {
				logger.error("Failed to send mail to person: " + lastName + ", " + firstName + "<" + recipient +"> for survey " + surveyName + beamLine, e);
			}
			
		}
		logger.info("Survey Notification Ended");
	}
	
    public void setMailSender(JavaMailSenderImpl mailSender){
        this.mailSender = mailSender;
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

	public void setSurveyParticipantNotificationDAO(SurveyParticipantNotificationDAO surveyParticipantNotificationDAO) {
		this.surveyParticipantNotificationDAO = surveyParticipantNotificationDAO;
	}

	public void setSurveyParticipantNotifyEventDAO(SurveyParticipantNotifyEventDAO surveyParticipantNotifyEventDAO) {
		this.surveyParticipantNotifyEventDAO = surveyParticipantNotifyEventDAO;
	}

	public void setLinkPrefix(String linkPrefix) {
		this.linkPrefix = linkPrefix;
	}
}
