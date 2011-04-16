package ca.sciencestudio.uso.survey.service.backers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyBacker {

	private int id;
	private String name;
	private String description;
	private int status;
	private Date deadline;
	private List<SurveySectionBacker> sectionBackers = new ArrayList<SurveySectionBacker>();
	private int sectionCount;
	private int questionCount;
	private int participantId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date date) {
		this.deadline = date;
	}

	public void setSectionBackers(List<SurveySectionBacker> sectionBackers) {
	    this.sectionBackers = sectionBackers;
	}

	public List<SurveySectionBacker> getSectionBackers() {
	    return sectionBackers;
	}

	public int getSectionCount() {
		return sectionCount;
	}

	public void setSectionCount(int sectionCount) {
		this.sectionCount = sectionCount;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

}

