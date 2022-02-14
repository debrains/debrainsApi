package com.debrains.debrainsApi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tier {
    BRONZE(1, Long.MIN_VALUE, 10L),
    SILVER(2, 11L, 20L),
    GOLD(3, 21L, 30L),
    PLATINUM(4, 31L, 40L),
    DIAMOND(5, 41L, 50L),
    MASTER(6, 51L, 60L),
    GRANDMASTER(7, 61L, 70L),
    CHALLENGE(8, 71L, Long.MAX_VALUE);

    private final int level;
    private final long start;
    private final long end;
}
