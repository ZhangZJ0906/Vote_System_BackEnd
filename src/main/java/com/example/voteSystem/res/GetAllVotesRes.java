package com.example.voteSystem.res;

import java.util.List;
import java.util.Map;

public class GetAllVotesRes extends BasicRes {
	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public GetAllVotesRes(String message, int code, List<Map<String, Object>> list) {
		super(message, code);
		this.list = list;
	}

	public GetAllVotesRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetAllVotesRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public GetAllVotesRes(String message, int code) {
		super(message, code);
		// TODO Auto-generated constructor stub
	}

}
