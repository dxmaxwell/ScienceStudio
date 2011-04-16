package ca.sciencestudio.uso.survey.service.backers;

import java.util.Date;


public class SurveyListBacker {
	private int id;
	private String name;
	private String description;
	private String status;
	private Date deadline;
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date date) {
		this.deadline = date;
	}

}

