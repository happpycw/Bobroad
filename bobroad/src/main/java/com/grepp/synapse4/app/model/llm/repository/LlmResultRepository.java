package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.entity.LLMResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LlmResultRepository extends JpaRepository<LLMResult, Long> {
    List<LLMResult> findByLlmQuestionId(Long questionId);
}
