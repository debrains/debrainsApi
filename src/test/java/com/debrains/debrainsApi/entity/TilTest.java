package com.debrains.debrainsApi.entity;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
@Log4j2
public class TilTest {

    private Object[] parametersForTotalCrtCnt() {
        return new Object[]{
                new Object[]{CycleStatus.EVERYDAY, 0, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 1, 30), 1, false},
                new Object[]{CycleStatus.EVERYDAY, 4, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 2, 20), 22, false},
                new Object[]{CycleStatus.EVERYDAY, 8, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 2, 20), 22, false},
                new Object[]{CycleStatus.WEEK, 3, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 21), 9, true},
                new Object[]{CycleStatus.WEEK, 4, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 2, 23), 16, false},
                new Object[]{CycleStatus.WEEK, 5, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 2, 23), 19, false},
                new Object[]{CycleStatus.WEEK, 7, LocalDate.of(2022, 1, 30), LocalDate.of(2022, 2, 23), 25, false},
                new Object[]{CycleStatus.EVERYDAY, 0, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 12), 43, false},
                new Object[]{CycleStatus.EVERYDAY, 0, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 2, 12), 408, false}
        };
    }

    @Test
    @Parameters(method = "parametersForTotalCrtCnt")
    public void totalCrtCnt(CycleStatus cycleStatus, int cycleCnt, LocalDate startDate, LocalDate endDate, int totalCnt, boolean isExpried) {
        // given
        Til til = Til.builder()
                .cycleStatus(cycleStatus)
                .cycleCnt(cycleCnt)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // when
        til.totalCrtCount();
        //til.expiredCheck();

        // then
        assertThat(til.getTotalCnt()).isEqualTo(totalCnt);
        //assertThat(til.isExpired()).isEqualTo(isExpried);
    }
}
