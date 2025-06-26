package com.grepp.synapse4.app.model.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyRequest {

    private String companyLocation;
    private String purpose;
    private String companion;
    private String favoriteCategory;
    private String preferredMood;
}
