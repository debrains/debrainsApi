package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TilService {

    Til updateTil(Long id, TilDTO tilDTO);

    Page<TilDTO> getAdminTilList(Pageable pageable);

    TilDTO getTil(Long id);
}
