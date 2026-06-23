package com.example.voteSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.voteSystem.req.AddVoteReq;
import com.example.voteSystem.res.BasicRes;
import com.example.voteSystem.services.VoteService;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/api/vote")
public class VoteController {
	@Autowired
	private VoteService voteService;

	@PostMapping
	public BasicRes addVote(AddVoteReq req) {

		return voteService.addVoteInfo(req);
	}
}
