package com.example.voteSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.voteSystem.dao.VoteDao;
import com.example.voteSystem.req.AddVoteReq;
import com.example.voteSystem.res.BasicRes;

@Service
public class VoteService {
	@Autowired
	private VoteDao voteDao;

	@Transactional
	public BasicRes addVoteInfo(AddVoteReq req) {

		return new BasicRes("投票成功", 200);
	}
}
