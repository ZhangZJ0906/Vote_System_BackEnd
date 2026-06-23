package com.example.voteSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vote_questions")
public class VoteQuestions {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "description")
	private String description;

	@Column(name = "title")
	private String title;

	@Column(name = "is_active")
	private boolean is_active;

	@Column(name = "created_at")
	private LocalDateTime created_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public VoteQuestions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VoteQuestions(int id, String description, String title, boolean is_active, LocalDateTime created_at) {
		super();
		this.id = id;
		this.description = description;
		this.title = title;
		this.is_active = is_active;
		this.created_at = created_at;
	}

}
