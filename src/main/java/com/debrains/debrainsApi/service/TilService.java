package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCurDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TilService {

    TilDTO createTil(TilDTO tilDTO);

    TilDTO updateTil(Long id, TilDTO tilDTO);

    Page<TilDTO> getAdminTilList(Pageable pageable);

    TilDTO getTil(Long id);

    List<TilDTO> getTilList(Long userId, Pageable pageable);

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
