package com.grepp.synapse4.app.model.llm.code;


public enum FavoriteCategory {

    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    FAST_FOOD("패스트푸드"),
    CAFE("카페/디저트"),
    BBQ("바비큐/그릴"),
    SEAFOOD("해산물"),
    VEGETARIAN("채식"),
    DESSERT("디저트"),
    SNACK("분식")
    ;

    private final String label;

    FavoriteCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
