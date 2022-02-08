package com.debrains.debrainsApi.validator;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TilValidator {

    public void validateDate(TilDTO tilDTO) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = tilDTO.getStartDate();
        LocalDate endDate = tilDTO.getEndDate();

        if(startDate.isBefore(now)){
            throw new ApiException(ErrorCode.NOW_BEFORE_STARTDATE);
        }

        if (endDate.isBefore(startDate)) {
            throw new ApiException(ErrorCode.ENDDATE_BEFORE_STARTDATE);
        }

        if (endDate.isBefore(now)) {
            throw new ApiException(ErrorCode.ENDDATE_BEFORE_NOW);
        }

        if (tilDTO.getCycleStatus().equals(CycleStatus.WEEK.toString()) && tilDTO.getCycleCnt() < 1) {
            throw new ApiException(ErrorCode.CYCLE_CNT_WRONG);
        }

    }
}
