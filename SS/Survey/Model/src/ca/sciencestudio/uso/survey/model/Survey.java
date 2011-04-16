package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;
import java.util.Date;


public class Survey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private int categoryId;
	private String description;
	private String participantDescription;
	private int status;
	private Date deadline;
	private Date createTime;
	

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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParticipantDescription() {
		return participantDescription;
	}

	public void setParticipantDescription(String participantDescription) {
		this.participantDescription = participantDescription;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}

