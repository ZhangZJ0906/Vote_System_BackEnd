package com.example.voteSystem.config;

import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return sanitize(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null)
			return null;
		return Arrays.stream(values).map(this::sanitize).toArray(String[]::new);
	}

	@Override
	public String getHeader(String name) {
		return sanitize(super.getHeader(name));
	}

	private String sanitize(String value) {
		if (value == null)
			return null;
		return value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'",
				"&#x27;");
	}
}