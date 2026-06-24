package com.example.voteSystem.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.voteSystem.entity.VoteQuestions;

@Repository
public interface VoteQuestionDao extends JpaRepository<VoteQuestions, Integer> {
	@Procedure(procedureName = "sp_create_vote_question")
	Long createVoteQuestion(@Param("p_title") String title, // 假設你 SP 裡寫 p_title
			@Param("p_description") String description // 假設你 SP 裡寫 p_description
	);

	@Procedure(procedureName = "sp_create_vote_item")
	void createVoteItem(@Param("p_question_id") Long questionId, @Param("p_item_name") String itemName);

	@Procedure(procedureName = "sp_delete_vote_question")
	void deleteVoteQuestion(@Param("p_question_id") Long questionId);

	@Query(value = "CALL sp_get_all_votes()", nativeQuery = true)
	List<Map<String, Object>> getAllVotes();

	@Procedure(procedureName = "sp_cast_vote_batch")
	void castVoteBatch(@Param("p_item_id") String itemId, @Param("p_user_id") Integer userId);

	@Query(value = "CALL sp_get_user_vote_history(:p_user_id)", nativeQuery = true)
	List<Map<String, Object>> getUserVoteHistory(@Param("p_user_id") Integer userId);
}
