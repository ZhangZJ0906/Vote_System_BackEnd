package com.example.voteSystem.res;

import java.util.List;

public class GetAllVotesRes<T> extends BasicRes {
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public GetAllVotesRes(String message, int code, List<T> list) {
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
