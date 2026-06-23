package com.example.voteSystem.req;

import jakarta.validation.constraints.NotBlank;

public class RegisterReq {
	@NotBlank(message = "名字不可為空")
	private String username;
	@NotBlank(message = "email不可為空")
	private String email;
	@NotBlank(message = "密碼不可為空")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RegisterReq(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public RegisterReq() {
		super();
		// TODO Auto-generated constructor stub
	}

}
