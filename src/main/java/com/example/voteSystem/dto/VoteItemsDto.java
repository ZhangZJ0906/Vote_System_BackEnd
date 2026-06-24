package com.example.voteSystem.dto;

public class VoteItemsDto {
	private Long itemId;
	private String itemName;
	private Integer voteCount;

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

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public VoteItemsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VoteItemsDto(Long itemId, String itemName, Integer voteCount) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.voteCount = voteCount;
	}
}
