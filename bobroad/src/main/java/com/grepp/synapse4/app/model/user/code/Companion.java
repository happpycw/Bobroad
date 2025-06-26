package com.grepp.synapse4.app.model.user.code;

import lombok.Getter;

@Getter
public enum Companion {

    COLLEAGUE("직장 동료"),
    BOSS("상사"),
    NEW_HIRE("신입 사원"),
    FAMILY("가족"),
    CLIENT("고객"),
    FRIEND("친구"),
    MENTOR("멘토"),
    PARTNER("비즈니스 파트너")
    ;

    private final String label;

    Companion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
