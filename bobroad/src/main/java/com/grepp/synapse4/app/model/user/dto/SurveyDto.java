package com.grepp.synapse4.app.model.user.dto;

import com.grepp.synapse4.app.model.user.code.*;
import com.grepp.synapse4.app.model.user.entity.Survey;
import lombok.*;

@Getter @Setter @ToString

public class SurveyDto {

    private Long surveyId;
    private String companyLocation;
    private String purpose;
    private String companion;
    private String favoriteCategory;
    private String preferMood;
    private Long userId;

    public static SurveyDto fromEntity(Survey s) {
        SurveyDto dto = new SurveyDto();
        dto.setSurveyId(s.getId());

        dto.setCompanyLocation(CompanyLocation.valueOf(s.getCompanyLocation()).getLabel());
        dto.setPurpose(Purpose.valueOf(s.getPurpose()).getLabel());
        dto.setCompanion(Companion.valueOf(s.getCompanion()).getLabel());
        dto.setFavoriteCategory(FavoriteCategory.valueOf(s.getFavoriteCategory()).getLabel());
        dto.setPreferMood(PreferredMood.valueOf(s.getPreferredMood()).getLabel());

        dto.setUserId(s.getUser().getId());
        return dto;
    }


}
