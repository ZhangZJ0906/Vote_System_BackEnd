package com.example.voteSystem.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.voteSystem.dao.VoteQuestionDao;
import com.example.voteSystem.dto.AddCastVoteDTO;
import com.example.voteSystem.dto.VoteHistoryItemDTO;
import com.example.voteSystem.dto.VoteHistoryQuestionDTO;
import com.example.voteSystem.dto.VoteItemsDto;
import com.example.voteSystem.dto.VoteQuestionsDto;
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
	public GetAllVotesRes<VoteQuestionsDto> getAllVotes() {
		List<Map<String, Object>> rawList = voteDao.getAllVotes();
		// 用 LinkedHashMap 保持 question 順序
		Map<Long, VoteQuestionsDto> questionMap = new LinkedHashMap<>();
		for (Map<String, Object> row : rawList) {
			Long questionId = ((Number) row.get("question_id")).longValue();
			String title = (String) row.get("question_title");
			String description = (String) row.get("question_description");
			Long itemId = row.get("item_id") != null ? ((Number) row.get("item_id")).longValue() : null;
			String itemName = (String) row.get("item_name");
			Integer voteCount = row.get("vote_count") != null ? ((Number) row.get("vote_count")).intValue() : 0;
			// 若此 question 還沒建，先建一個空的
			questionMap.computeIfAbsent(questionId,
					id -> new VoteQuestionsDto(id, title, description, new ArrayList<>()));
			// 有 item 才加進去（LEFT JOIN 可能有 null）
			if (itemId != null) {
				questionMap.get(questionId).getItems().add(new VoteItemsDto(itemId, itemName, voteCount));
			}
		}
		List<VoteQuestionsDto> result = new ArrayList<>(questionMap.values());
		return new GetAllVotesRes<>("成功", 200, result);
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
	public GetAllVotesRes<VoteHistoryQuestionDTO> getUserVoteHistory(Integer userId) {
		if (userId == null) {
			return new GetAllVotesRes<>("使用者ID 為空", 400);
		}

		List<Map<String, Object>> rawList = voteDao.getUserVoteHistory(userId);
		System.out.println("rawList size: " + rawList.size());
		System.out.println("rawList: " + rawList);
		List<Map<String, Object>> normalizedList = rawList.stream().map(row -> {
			Map<String, Object> normalized = new LinkedHashMap<>();
			row.forEach((k, v) -> normalized.put(k.toLowerCase(), v));
			return normalized;
		}).collect(Collectors.toList());

		List<VoteHistoryQuestionDTO> result = normalizedList.stream()

				.filter(row -> row.get("question_id") != null)
				.collect(Collectors.groupingBy(row -> ((Number) row.get("question_id")).longValue(), LinkedHashMap::new,
						Collectors.toList()))
				.entrySet().stream().map(entry -> {
					Map<String, Object> first = entry.getValue().get(0);
					List<VoteHistoryItemDTO> items = entry.getValue().stream().filter(row -> row.get("item_id") != null)
							.map(row -> new VoteHistoryItemDTO(((Number) row.get("item_id")).longValue(),
									(String) row.get("item_name"),
									row.get("voted_at") != null ? row.get("voted_at").toString() : null))
							.collect(Collectors.toList());

					return new VoteHistoryQuestionDTO(entry.getKey(), (String) first.get("question_title"), items);
				}).collect(Collectors.toList());

		return new GetAllVotesRes<>("查詢成功", 200, result);
	}
}