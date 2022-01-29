package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;

public interface TilService {

    TilDTO createTil(TilDTO tilDTO);
}
