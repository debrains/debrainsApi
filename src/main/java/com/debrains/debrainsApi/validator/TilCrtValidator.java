package com.debrains.debrainsApi.validator;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TilCrtValidator {

    public void validateDate(TilCrtDTO tilCrtDTO) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = tilCrtDTO.getStartTime();
        LocalDateTime endTime = tilCrtDTO.getEndTime();

        if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
            throw new ApiException(ErrorCode.NO_DATA_TIME);
        }

        if (startTime != null && endTime != null) {
            if(startTime.isAfter(now)){
                throw new ApiException(ErrorCode.NOW_BEFORE_STARTTIME);
            }

            if (endTime.isBefore(startTime)) {
                throw new ApiException(ErrorCode.ENDTIME_BEFORE_STARTTIME);
            }

            if(endTime.isAfter(now)){
                throw new ApiException(ErrorCode.NOW_BEFORE_ENDTIME);
            }
        }
    }

    public void validateCrt(TilCrtDTO tilCrtDTO, MultipartFile[] files) {
        if (files == null && tilCrtDTO.getStartTime() == null && tilCrtDTO.getWatchTime() == null) {
            throw new ApiException(ErrorCode.NO_DATA_CRT);
        }
    }
}
