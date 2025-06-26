package com.grepp.synapse4.app.model.user.code;

import lombok.Getter;

@Getter
public enum CompanyLocation {

    APGUJEONG("압구정동"),
    SINSA("신사동"),
    NONHYEON("논현동"),
    CHEONGDAM("청담동"),
    SAMSEONG("삼성동"),
    YEOKSAM("역삼동"),
    DOGOK("도곡동"),
    DAECHI("대치동"),
    GAPPO("개포동"),
    IRWON("일원동"),
    SUSEO("수서동"),
    JAGOK("자곡동"),
    SEGOK("세곡동"),
    YULHYEON("율현동");

    private final String label;

    CompanyLocation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
