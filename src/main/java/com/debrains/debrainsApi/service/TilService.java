package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.User;

public interface TilService {

    Til createTil(TilDTO tilDTO);

    Til updateTil(Long id, TilDTO tilDTO);

    default Til dtoToEntity(TilDTO tilDTO) {
        User user = User.builder()
                .id(tilDTO.getUserId())
                .build();

        Til til = Til.builder()
                .user(user)
                .subject(tilDTO.getSubject())
                .description(tilDTO.getDescription())
                .startDate(tilDTO.getStartDate())
                .endDate(tilDTO.getEndDate())
                .cycleStatus(CycleStatus.valueOf(tilDTO.getCycleStatus()))
                .cycleCnt(tilDTO.getCycleCnt())
                .build();

        return til;
    }
}
