package com.example.voteSystem.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.voteSystem.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	// 登入
	@Procedure(procedureName = "sp_login_user")
	Optional<User> loginUserWithSp(@Param("email") String email, @Param("password") String password);

//註冊
	@Procedure(procedureName = "sp_register_user", outputParameterName = "p_result")
	String registerUserWithSp(@Param("p_email") String email, @Param("p_password") String password,
			@Param("p_username") String username);

	// check Email
	@Procedure(procedureName = "sp_check_user_email")
	Integer checkUserEmailWithSp(@Param("p_email") String email);
}
