package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.dto.AdminCurationDto;
import com.grepp.synapse4.app.model.llm.dto.CurationRestaurantDto;
import com.grepp.synapse4.app.model.llm.dto.UserCurationDto;
import com.grepp.synapse4.app.model.llm.dto.UserCurationSurveyDto;
import com.grepp.synapse4.app.model.llm.entity.Curation;
import com.grepp.synapse4.app.model.llm.repository.CurationRepository;
import com.grepp.synapse4.app.model.user.entity.Survey;
import com.grepp.synapse4.app.model.user.repository.SurveyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;
    private final SurveyRepository surveyRepository;

    @Transactional
    public Curation setCuration(AdminCurationDto dto) {

        // 1. 큐레이션 엔티티 저장
        Curation curation = new Curation();
        curation.setTitle(dto.getTitle());
        curation.setCompanyLocation(String.valueOf(dto.getCompanyLocation()));
        curation.setCompanion(String.valueOf(dto.getCompanion()));
        curation.setPurpose(String.valueOf(dto.getPurpose()));
        curation.setFavoriteCategory(String.valueOf(dto.getFavoriteCategory()));
        curation.setPreferredMood(String.valueOf(dto.getPreferredMood()));

        curationRepository.save(curation);

        return curation;

    }


    //     우선 가장 최신 등록한 큐레이션만 노출하도록 로직 설계
    //     4개 테이블 조인해서 lazy 이슈로 서비스단에서 트랜젝션 리드온니처리
    @Transactional(readOnly = true)
    public Optional<UserCurationDto> getLatestCurationRestaurants() {

        // 1. 최신 큐레이션 엔티티
        Curation latest = curationRepository.findTopByOrderByCreatedAtDesc();

        if (ObjectUtils.isEmpty(latest)) {
            return Optional.empty();
        }

        if (latest.getResults() == null || latest.getResults().isEmpty()) {
            return Optional.empty();
        }


        // 2. 최신 큐레이션 엔티티의 결과 리스트 엔티티
        // 3. 큐레이션 결과의 1개 식당과 매핑
        //    그렇게 매핑된 식당을 '큐레이션식당 dto'에 담기
        List<CurationRestaurantDto> dtos = latest.getResults().stream()
                .map(result -> CurationRestaurantDto.fromEntity(result.getRestaurant(), result))
                .toList();

        return Optional.of(new UserCurationDto(
                latest.getId(),
                latest.getTitle(),
                dtos));
    }

    // 추천 메서드
    public UserCurationSurveyDto recommendCurationSurveys(Long userId) {
        Survey survey = loadUserSurvey(userId);
        List<Curation> allCurations = curationRepository.findCurationIdWithRestaurantId();
        Optional<Curation> best = findBestCurationMatch(survey, allCurations);
        return best.map(this::buildSurveyDtoFromCuration).orElseGet(this::buildEmptySurveyDto);
    }

    // 비어있는 dto 반환
    private UserCurationSurveyDto buildEmptySurveyDto() {
        return new UserCurationSurveyDto(null,"큐레이션이 없습니다.", List.of());
    }

    // 유저 설문조회
    private Survey loadUserSurvey(Long userId) {
        return surveyRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당유저의 설문조사가 없습니다."));
    }

    // 최초 점수 큐레이션
    private Optional<Curation> findBestCurationMatch(Survey survey, List<Curation> curations) {
        return curations.stream()
                .map(c -> new MatchPair(
                        c,
                        calcScore(
                                survey.getCompanyLocation(),
                                survey.getPurpose(),
                                survey.getCompanion(),
                                survey.getFavoriteCategory(),
                                survey.getPreferredMood(),

                                c.getCompanyLocation(),
                                c.getPurpose(),
                                c.getCompanion(),
                                c.getFavoriteCategory(),
                                c.getPreferredMood()
                        )
                ))
                .max(Comparator.comparingInt(MatchPair::getScore))
                .map(MatchPair::getCuration);
    }

    // dto 생성
    private UserCurationSurveyDto buildSurveyDtoFromCuration(Curation best) {
        List<UserCurationSurveyDto.RestaurantDto> restaurants = best.getResults().stream()
                .map(cr -> CurationRestaurantDto.fromEntity(cr.getRestaurant(), cr))
                .map(dto -> new UserCurationSurveyDto.RestaurantDto(
                        dto.getId(),
                        dto.getName(),
                        dto.getCategory(),
                        dto.getRoadAddress(),
                        dto.getBranch(),
                        dto.getReason(),
                        dto.getBusinessTime()
                ))
                .limit(3)
                .toList();

        return new UserCurationSurveyDto(
                best.getId(),
                best.getTitle(),
                restaurants
        );
    }

    // 점수 계산기
    private int calcScore(
            String uLoc, String uPurpose, String uCompanion, String uCategory, String uMood,
            String cLoc, String cPurpose, String cCompanion, String cCategory, String cMood
    ) {
        int score = 0;
        if (uLoc.equalsIgnoreCase(cLoc)) score += 3;
        if (uPurpose.equalsIgnoreCase(cPurpose)) score += 2;
        if (uCompanion.equalsIgnoreCase(cCompanion)) score += 2;
        if (uCategory.equalsIgnoreCase(cCategory)) score += 1;
        if (uMood.equalsIgnoreCase(cMood)) score += 1;
        return score;
    }

    @Getter
    private static class MatchPair {
        private final Curation curation;
        private final int score;

        public MatchPair(Curation curation, int score) {
            this.curation = curation;
            this.score = score;
        }
    }
}