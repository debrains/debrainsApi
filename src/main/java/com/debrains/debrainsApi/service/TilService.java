package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCurDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TilService {

    Til createTil(TilDTO tilDTO);

    Til updateTil(Long id, TilDTO tilDTO);

    Page<TilDTO> getAdminTilList(Pageable pageable);

    TilDTO getTil(Long id);

    Page<Til> getTilList(Pageable pageable);

    TilCurDTO currentTil(Long userId);

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
