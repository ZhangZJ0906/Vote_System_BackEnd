package com.example.voteSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_votes")
public class UserVote {
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "vote_item_id")
	private Integer voteItemId;
	@Column(name = "created_at")
	private LocalDateTime created_at;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVoteItemId() {
		return voteItemId;
	}

	public void setVoteItemId(Integer voteItemId) {
		this.voteItemId = voteItemId;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public UserVote(Integer id, Integer userId, Integer voteItemId, LocalDateTime created_at) {
		super();
		this.id = id;
		this.userId = userId;
		this.voteItemId = voteItemId;
		this.created_at = created_at;
	}

	public UserVote() {
		super();
		// TODO Auto-generated constructor stub
	}

}
