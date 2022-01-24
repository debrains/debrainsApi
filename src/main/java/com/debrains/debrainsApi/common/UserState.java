package com.debrains.debrainsApi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserState {
    ACT("ACT", "사용중"),
    STOP("STOP", "중지"),
    DEL("DEL", "탈퇴");

    private final String state;
    private final String description;
}
