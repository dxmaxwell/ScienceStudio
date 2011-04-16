package ca.sciencestudio.uso.survey.service.backers;

public class SurveyQuestionListBacker {
	private int id;
	private int order;
	private String text;
	private String type;
	private int answerLength;
	private String image;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

