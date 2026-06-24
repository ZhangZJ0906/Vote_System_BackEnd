package com.example.voteSystem.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.voteSystem.dao.UserDao;
import com.example.voteSystem.entity.User;
import com.example.voteSystem.req.LoginReq;
import com.example.voteSystem.req.RegisterReq;
import com.example.voteSystem.res.BasicRes;
import com.example.voteSystem.res.LoginUserRes;

@Service
public class UserServices {
	@Autowired
	private UserDao userDao;
	// 密碼Hash
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public LoginUserRes login(LoginReq req) {
		try {
			String email = req.getEmail();
			String rawPassword = req.getPassword();
			Optional<User> userOpt = userDao.loginUserWithSp(email, "");

			if (userOpt.isEmpty()) {
				return new LoginUserRes("帳號或密碼錯誤", 400);
			}
			User user = userOpt.get();
			if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
				return new LoginUserRes("帳號或密碼錯誤", 400);
			}
			return new LoginUserRes("成功登入", 200, user.getId(), user.getUsername(), user.getRole(), user.getEmail());
		} catch (Exception e) {
			return new LoginUserRes("帳號或密碼錯誤", 400);
		}

	}

	@Transactional
	public BasicRes register(RegisterReq req) {
		String email = req.getEmail();

		String name = req.getUsername();
		String hashedPassword = passwordEncoder.encode(req.getPassword());

		String user = userDao.registerUserWithSp(email, hashedPassword, name);

		if ("EMAIL_EXISTS".equals(user)) {
			return new BasicRes("Email重複", 400);
		}
		return new BasicRes("成功註冊", 200);
	}
}
