package com.debrains.debrainsApi.validator;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@Component
public class TilValidator {

    public void validate(TilDTO tilDTO, Errors errors) {

        LocalDate now = LocalDate.now();
        LocalDate endDate = tilDTO.getEndDate();
        if (endDate.isBefore(tilDTO.getStartDate())) {
            errors.rejectValue("endDate", "wrongValue", "EndDate is wrong.");
            errors.rejectValue("startDate", "wrongValue", "StartDate is wrong.");
        }

        if (endDate.isBefore(now)) {
            errors.rejectValue("endDate", "wrongValue", "EndDate is wrong.");
        }

        if (tilDTO.getCycleStatus().equals(CycleStatus.WEEK.toString()) && tilDTO.getCycleCnt() < 1) {
            errors.rejectValue("cycleCnt", "wrongValue", "CycleCnt is wrong.");
        }
    }
}
