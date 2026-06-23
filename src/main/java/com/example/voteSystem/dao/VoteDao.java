package com.example.voteSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.voteSystem.entity.VoteItem;

@Repository
public interface VoteDao extends JpaRepository<VoteItem, Integer> {

}
