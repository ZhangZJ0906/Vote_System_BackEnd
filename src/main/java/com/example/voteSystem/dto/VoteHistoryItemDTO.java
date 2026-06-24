package com.example.voteSystem.dto;

public class VoteHistoryItemDTO {
	private Long itemId;
	private String itemName;
	private String votedAt;

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

	public VoteHistoryItemDTO(Long itemId, String itemName, String votedAt) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.votedAt = votedAt;
	}

	public VoteHistoryItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
