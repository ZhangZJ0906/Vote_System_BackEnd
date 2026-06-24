package com.example.voteSystem.dto;

import java.util.List;

public class VoteHistoryQuestionDTO {
	private Long questionId;
	private String questionTitle;
	private List<VoteHistoryItemDTO> items;

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

	public List<VoteHistoryItemDTO> getItems() {
		return items;
	}

	public void setItems(List<VoteHistoryItemDTO> items) {
		this.items = items;
	}

	public VoteHistoryQuestionDTO(Long questionId, String questionTitle, List<VoteHistoryItemDTO> items) {
		super();
		this.questionId = questionId;
		this.questionTitle = questionTitle;
		this.items = items;
	}

	public VoteHistoryQuestionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
