package com.grepp.synapse4.app.model.user.code;

import lombok.Getter;

@Getter
public enum Purpose {

    NETWORKING("네트워킹"),
    FOODIE("미식가"),
    FAST_EATER("빠른 식사"),
    HEALTHY("건강식 선호"),
    BUDGET_SENSITIVE("가성비 중시"),
    TALKATIVE("수다쟁이"),
    QUIET("조용한 식사"),
    TEAM_BUILDING("팀워크 강화"),
    CELEBRATION("기념일/회식"),
    NEWCOMER("신입 환영")
    ;

    private final String label;

    Purpose(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
