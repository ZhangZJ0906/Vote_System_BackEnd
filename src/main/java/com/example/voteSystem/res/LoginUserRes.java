package com.example.voteSystem.res;

public class LoginUserRes extends BasicRes {
	private int id;
	private String username;
	private String role;
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LoginUserRes(String message, int code, int id, String username, String role, String email) {
		super(message, code);
		this.id = id;
		this.username = username;
		this.role = role;
		this.email = email;
	}
	public LoginUserRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginUserRes(String message, int code) {
		super(message, code);
		// TODO Auto-generated constructor stub
	}


}
