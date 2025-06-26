package com.grepp.synapse4.app.model.user.code;

import lombok.Getter;

@Getter
public enum PreferredMood {

    CASUAL("캐주얼"),
    ROMANTIC("로맨틱"),
    ENERGETIC("활기찬"),
    COZY("아늑한"),
    QUIET("조용한"),
    FESTIVE("축제 분위기"),
    BUSINESS("비즈니스"),
    FAMILY_FRIENDLY("가족 친화적"),
    TRENDY("트렌디"),
    RETRO("레트로")
    ;

    private final String label;

    PreferredMood(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
