package com.debrains.debrainsApi.validator;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Date;

@Component
public class TilValidator {

    public void validate(TilDTO tilDTO, Errors errors) {

        Date endDate = tilDTO.getEndDate();
        if (endDate.before(tilDTO.getStartDate())) {
            errors.rejectValue("endDate", "wrongValue", "EndDate is wrong.");
            errors.rejectValue("startDate", "wrongValue", "StartDate is wrong.");
        }

        System.out.println(tilDTO.getCycleStatus().equals(CycleStatus.WEEK.toString()));
        if (tilDTO.getCycleStatus().equals(CycleStatus.WEEK.toString()) && tilDTO.getCycleCnt() < 1) {
            errors.rejectValue("cycleCnt", "wrongValue", "CycleCnt is wrong.");
        }
    }
}
