package com.grepp.synapse4.app.model.meeting.code;

import lombok.Getter;

@Getter
public enum Purpose {
  LUNCH("점심"),
  DINING_TOGETHER("회식"),
  STUDY("스터디");

  private final String displayName;

  Purpose(String displayName) {
    this.displayName = displayName;
  }
}

