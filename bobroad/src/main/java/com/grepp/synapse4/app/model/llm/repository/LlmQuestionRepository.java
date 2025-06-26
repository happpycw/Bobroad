package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.entity.LLMQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LlmQuestionRepository extends JpaRepository<LLMQuestion, Long> {
}
