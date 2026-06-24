package com.example.voteSystem.dto;

import java.util.List;

public class VoteQuestionsDto {
	private Long questionId;
	private String questionTitle;
	private String questionDescription;
	private List<VoteItemsDto> items;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getQuestionDescription() {
		return questionDescription;
	}

	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	public List<VoteItemsDto> getItems() {
		return items;
	}

	public void setItems(List<VoteItemsDto> items) {
		this.items = items;
	}

	public VoteQuestionsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VoteQuestionsDto(Long questionId, String questionTitle, String questionDescription,
			List<VoteItemsDto> items) {
		super();
		this.questionId = questionId;
		this.questionTitle = questionTitle;
		this.questionDescription = questionDescription;
		this.items = items;
	}

}
