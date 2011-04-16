package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;


public class SurveyQuestion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int sectionId;
	private int order;
	private String text;
	private int type;
	private int answerLength;
	private String image;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getSectionId(){
		return sectionId;
	}
	
	public void setSectionId(int sectionId){
		this.sectionId = sectionId;
	}
	
	public int getOrder(){
		return order;
	}
	
	public void setOrder(int order){
		this.order = order;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAnswerLength() {
		return answerLength;
	}

	public void setAnswerLength(int answerLength) {
		this.answerLength = answerLength;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}

