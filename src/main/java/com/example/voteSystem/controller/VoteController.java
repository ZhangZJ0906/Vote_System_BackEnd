package com.example.voteSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.voteSystem.dto.AddCastVoteDTO;
import com.example.voteSystem.req.AddVoteReq;
import com.example.voteSystem.res.BasicRes;
import com.example.voteSystem.res.GetAllVotesRes;
import com.example.voteSystem.services.VoteService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/api/vote")
public class VoteController {
	@Autowired
	private VoteService voteService;

// 建立 投票標題跟 選項
	@PostMapping(value = "/add")
	public BasicRes addVote(@Valid @RequestBody AddVoteReq req) {

		return voteService.createVote(req);
	}

// 刪除 投票 與該項目的 選項
	@PostMapping("/delete/{id}")
	public BasicRes deleteVote(@PathVariable("id") Long id) {

		return voteService.deleteVote(id);
	}

// 獲取所有投票列表及累積票數
	@GetMapping("/list")
	public GetAllVotesRes getAllVotes() {
		return voteService.getAllVotes();
	}

//使用者投票 (多選)
	@PostMapping("/cast")
	public BasicRes castVotes(@RequestBody AddCastVoteDTO dto) {

		return voteService.castVotes(dto);
	}

	// 查看歷史投票
	@GetMapping("/history")
	public GetAllVotesRes getUserVoteHistory(@RequestParam("userId") Integer userId) {
		return voteService.getUserVoteHistory(userId);
	}

}
