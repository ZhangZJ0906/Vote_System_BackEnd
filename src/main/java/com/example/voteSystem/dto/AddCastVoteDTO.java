package com.example.voteSystem.dto;

import java.util.List;

public class AddCastVoteDTO {
	private Integer userId;
	private List<Long> itemIds;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}

	public AddCastVoteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddCastVoteDTO(Integer userId, List<Long> itemIds) {
		super();
		this.userId = userId;
		this.itemIds = itemIds;
	}

}
