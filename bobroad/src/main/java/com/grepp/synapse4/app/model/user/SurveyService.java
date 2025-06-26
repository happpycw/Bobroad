package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.code.Companion;
import com.grepp.synapse4.app.model.user.code.CompanyLocation;
import com.grepp.synapse4.app.model.user.code.FavoriteCategory;
import com.grepp.synapse4.app.model.user.code.PreferredMood;
import com.grepp.synapse4.app.model.user.code.Purpose;
import com.grepp.synapse4.app.model.user.dto.request.SurveyRequest;
import com.grepp.synapse4.app.model.user.entity.Survey;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.SurveyRepository;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    // 설문 최초 제출
    public void submitSurvey(SurveyRequest request, User user) {
        Survey survey = mapToSurveyEntity(request);
        survey.setUser(user);

        surveyRepository.save(survey);

        user.setIsSurvey(true);
        userRepository.save(user);
    }

    // 설문 수정
    public void updateSurvey(Long id, SurveyRequest request, User user) {
        Survey survey = surveyRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 설문이 존재하지 않습니다."));

        if (!survey.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("설문 수정 권한이 없습니다.");
        }

        updateSurveyFromRequest(survey, request);
    }

    // 설문 -> SurveyRequest DTO
    public SurveyRequest getSurveyRequest(User user) {
        Survey survey = surveyRepository.findByUser(user)
            .orElseThrow(() -> new NoSuchElementException("설문이 존재하지 않습니다."));

        return mapToSurveyRequest(survey);
    }

    // 설문 ID 조회
    public Long getSurveyId(User user) {
        return surveyRepository.findByUser(user)
            .map(Survey::getId)
            .orElseThrow(() -> new NoSuchElementException("설문 없음"));
    }

    private Survey mapToSurveyEntity(SurveyRequest request) {
        Survey survey = new Survey();
        updateSurveyFromRequest(survey, request);
        return survey;
    }

    public Optional<SurveyRequest> findSurveyRequest(Long userId) {
        return surveyRepository.findByUserId(userId)
            .map(this::mapToSurveyRequest);
    }

    private void updateSurveyFromRequest(Survey survey, SurveyRequest request) {
        survey.setCompanyLocation(CompanyLocation.valueOf(request.getCompanyLocation()).name());
        survey.setPurpose(Purpose.valueOf(request.getPurpose()).name());
        survey.setCompanion(Companion.valueOf(request.getCompanion()).name());
        survey.setFavoriteCategory(FavoriteCategory.valueOf(request.getFavoriteCategory()).name());
        survey.setPreferredMood(PreferredMood.valueOf(request.getPreferredMood()).name());
    }

    private SurveyRequest mapToSurveyRequest(Survey survey) {
        SurveyRequest dto = new SurveyRequest();
        dto.setCompanyLocation(survey.getCompanyLocation());
        dto.setPurpose(survey.getPurpose());
        dto.setCompanion(survey.getCompanion());
        dto.setFavoriteCategory(survey.getFavoriteCategory());
        dto.setPreferredMood(survey.getPreferredMood());
        return dto;
    }
}
