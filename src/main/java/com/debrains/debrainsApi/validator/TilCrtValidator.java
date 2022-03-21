package com.debrains.debrainsApi.validator;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
public class TilCrtValidator {

    public void validateDate(TilCrtDTO tilCrtDTO) {

        check(tilCrtDTO.getStartTime1(), tilCrtDTO.getEndTime1());
        check(tilCrtDTO.getStartTime2(), tilCrtDTO.getEndTime2());
        check(tilCrtDTO.getStartTime3(), tilCrtDTO.getEndTime3());
    }

    private void check(LocalDateTime startTime, LocalDateTime endTime) {

        LocalDateTime now = LocalDateTime.now();

        if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
            throw new ApiException(ErrorCode.NO_DATA_TIME);
        }

        if (startTime != null && endTime != null) {
            if (startTime.isAfter(now)) {
                throw new ApiException(ErrorCode.NOW_BEFORE_STARTTIME);
            }

            if (endTime.isBefore(startTime)) {
                throw new ApiException(ErrorCode.ENDTIME_BEFORE_STARTTIME);
            }

            if (endTime.isAfter(now)) {
                throw new ApiException(ErrorCode.NOW_BEFORE_ENDTIME);
            }
        }
    }

    public void validateCrt(TilCrtDTO tilCrtDTO, MultipartFile[] files) {
        if (files == null && tilCrtDTO.getStartTime1() == null && tilCrtDTO.getStartTime2() == null
                && tilCrtDTO.getStartTime3() == null && tilCrtDTO.getWatchTime() == null) {
            throw new ApiException(ErrorCode.NO_DATA_CRT);
        }
    }
}
