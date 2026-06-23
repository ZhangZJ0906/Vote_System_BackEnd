package com.example.voteSystem.req;

import jakarta.validation.constraints.NotBlank;

public class LoginReq {
	@NotBlank(message = "信箱不可為空")
	private String email;
	@NotBlank(message = "密碼不可為空")
	private String password;

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

	public LoginReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginReq(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

}
