package com.example.voteSystem.req;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class AddVoteReq {
	@NotBlank(message = "標題不可為空")
	private String title;

	private String description;
	@NotEmpty(message = "選項不可為空")
	private List<String> itemNames;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getItemNames() {
		return itemNames;
	}

	public void setItemNames(List<String> itemNames) {
		this.itemNames = itemNames;
	}

	public AddVoteReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddVoteReq(String title, String description, List<String> itemNames) {
		super();
		this.title = title;
		this.description = description;
		this.itemNames = itemNames;
	}

}
