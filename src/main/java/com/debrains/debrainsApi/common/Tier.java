package com.debrains.debrainsApi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tier {
    BRONZE(1, Long.MIN_VALUE, 500L),          // 500
    SILVER(2, 501L, 2000L),             // 1500
    GOLD(3, 2001L, 5000L),              // 3000
    PLATINUM(4, 5001L, 11000L),         // 6000
    DIAMOND(5, 11001L, 23000L),         // 12000
    MASTER(6, 23001L, 47000L),          // 24000
    GRANDMASTER(7, 47001L, 100000L),    // 53000
    CHALLENGE(8, 100001L, Long.MAX_VALUE);

    private final int level;
    private final long start;
    private final long end;
}
