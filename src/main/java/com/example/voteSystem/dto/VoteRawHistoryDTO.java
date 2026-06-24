package com.example.voteSystem.dto;

public class VoteRawHistoryDTO {
	private Long questionId;
	private String questionTitle;
	private Long itemId;
	private String itemName;
	private String votedAt;

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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getVotedAt() {
		return votedAt;
	}

	public void setVotedAt(String votedAt) {
		this.votedAt = votedAt;
	}

	public VoteRawHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VoteRawHistoryDTO(Long questionId, String questionTitle, Long itemId, String itemName, String votedAt) {
		super();
		this.questionId = questionId;
		this.questionTitle = questionTitle;
		this.itemId = itemId;
		this.itemName = itemName;
		this.votedAt = votedAt;
	}

}
