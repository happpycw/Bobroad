package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.dto.SurveyDto;
import com.grepp.synapse4.app.model.user.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PreferService {

    private final SurveyRepository surveyRepository;

    public List<SurveyDto> getUserPreferences(Long userId) {
        return surveyRepository.findByUserId(userId).stream()
                .map(SurveyDto::fromEntity)
                .toList();
    }
}
