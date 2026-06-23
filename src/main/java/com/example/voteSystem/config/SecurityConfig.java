package com.example.voteSystem.config;

import java.util.List; // 記得匯入

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 1. 💡 啟用 CORS 設定
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// 必須關閉 CSRF 功能
				.csrf(csrf -> csrf.disable())

				// 設定權限規則
				.authorizeHttpRequests(auth -> auth
						// 💡 確保 OPTIONS 請求完全放行（這步非常關鍵！）
						.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/user/**").permitAll()
						// 放行投票相關端點（或者你可以依需求設定成需登入，目前先全放行測試）
						.requestMatchers("/api/vote/**").permitAll().anyRequest().authenticated());

		return http.build();
	}

	// 2. 💡 定義 CORS 的具體規則
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// 允許來自前端 4000 埠的請求
		configuration.setAllowedOrigins(List.of("http://localhost:4000"));
		// 允許的 HTTP 方法
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// 允許的 Header
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
		// 是否允許攜帶 Cookie 等憑證
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 套用到所有的 API 路徑
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}