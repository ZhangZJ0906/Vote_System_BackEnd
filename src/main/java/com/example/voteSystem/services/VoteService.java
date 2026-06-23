package com.example.voteSystem.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.voteSystem.dao.VoteQuestionDao;
import com.example.voteSystem.dto.AddCastVoteDTO;
import com.example.voteSystem.req.AddVoteReq;
import com.example.voteSystem.res.BasicRes;
import com.example.voteSystem.res.GetAllVotesRes;

@Service
public class VoteService {
	@Autowired
	private VoteQuestionDao voteDao;

//建立
	@Transactional(rollbackFor = Exception.class)
	public BasicRes createVote(AddVoteReq req) {

		Long newQuestionId = voteDao.createVoteQuestion(req.getTitle(), req.getDescription());

		if (newQuestionId == null) {
			throw new RuntimeException("無法取得新增主題的識別碼");
		}

		if (req.getItemNames() != null) {
			for (String itemName : req.getItemNames()) {
				voteDao.createVoteItem(newQuestionId, itemName);
			}
		}
		return new BasicRes("新曾成功", 200);
	}

//刪除
	@Transactional(rollbackFor = Exception.class)
	public BasicRes deleteVote(Long questionId) {
		if (questionId == null) {
			return new BasicRes(400, "請提供正確的投票主題識別碼");
		}

		//
		voteDao.deleteVoteQuestion(questionId);

		return new BasicRes(200, "投票主題與相關選項已成功刪除！");
	}

// 投票項目跟選項
	@Transactional
	public GetAllVotesRes getAllVotes() {
		List<Map<String, Object>> list = voteDao.getAllVotes();
		return new GetAllVotesRes("成功", 200, list);
	}

//批次投票
	@Transactional(rollbackFor = Exception.class)
	public BasicRes castVotes(AddCastVoteDTO dto) {
		if (dto == null || dto.getItemIds() == null || dto.getItemIds().isEmpty()) {
			return new BasicRes(400, "請至少選擇一個投票項目");
		}
		if (dto.getUserId() == null) {
			return new BasicRes(400, "缺少使用者識別碼");
		}

		String idsString = dto.getItemIds().stream().map(String::valueOf).collect(Collectors.joining(","));

		voteDao.castVoteBatch(idsString, dto.getUserId());

		return new BasicRes(200, "投票成功！感謝您的參與");
	}

//查看歷史投票
	@Transactional
	public GetAllVotesRes getUserVoteHistory(Integer userId) {
		if (userId == null) {
			return new GetAllVotesRes("使用者ID 為空", 400);
		}
		List<Map<String, Object>> list = voteDao.getUserVoteHistory(userId);
		return new GetAllVotesRes("成功", 200, list);
	}
}
