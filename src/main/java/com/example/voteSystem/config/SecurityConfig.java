package com.example.voteSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// 1. 把 BCrypt 註冊為 Bean，這樣你 Service 的 @Autowired 密碼加密器才不會報錯
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 2. 設定自訂的過濾連鎖，把特定的 API 放行
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 必須關閉 CSRF 功能，否則 POST 請求會被 403 擋掉
				.csrf(csrf -> csrf.disable())

				// 設定權限規則
				.authorizeHttpRequests(auth -> auth
						// 💡 關鍵： permitAll() 代表「完全放行」，不需要任何登入或權限
						.requestMatchers("/api/user/register", "/api/user/login").permitAll()

						// 剩下的其他所有 API 請求（anyRequest），都還是需要通過驗證（authenticated）
						.anyRequest().authenticated());

		return http.build();
	}
}