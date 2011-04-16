package ca.sciencestudio.uso.survey.service.backers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SurveyStatisticBacker {

	private String name;
	private String description;
	private int status;
	private Date deadline;
	private int expectedParticipantCount;
	private int attendedParticipantCount;
	private double participateRate;
	private List<SurveyStatisticDetailBacker> statisticList = new ArrayList<SurveyStatisticDetailBacker>();

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

	public int getExpectedParticipantCount() {
		return expectedParticipantCount;
	}

	public void setExpectedParticipantCount(int expectedParticipantCount) {
		this.expectedParticipantCount = expectedParticipantCount;
	}

	public int getAttendedParticipantCount() {
		return attendedParticipantCount;
	}

	public void setAttendedParticipantCount(int attendedParticipantCount) {
		this.attendedParticipantCount = attendedParticipantCount;
	}

	public double getParticipateRate() {
		return participateRate;
	}

	public void setParticipateRate(double participateRate) {
		this.participateRate = participateRate;
	}
	
	public void setStatisticList(List<SurveyStatisticDetailBacker> statisticList) {
	    this.statisticList = statisticList;
	}

	public List<SurveyStatisticDetailBacker> getStatisticList() {
	    return statisticList;
	}	
}
