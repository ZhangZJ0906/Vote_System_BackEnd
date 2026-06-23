package com.example.voteSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.voteSystem.req.LoginReq;
import com.example.voteSystem.req.RegisterReq;
import com.example.voteSystem.res.BasicRes;
import com.example.voteSystem.res.LoginUserRes;
import com.example.voteSystem.services.UserServices;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserServices userServices;

//登入
	@PostMapping(value = "/login")
	public LoginUserRes login(@Valid @RequestBody LoginReq req) {
		return userServices.login(req);

	}

	// 註冊
	@PostMapping(value = "/register")
	public BasicRes register(@Valid @RequestBody RegisterReq req) {
		return userServices.register(req);

	}
}
