package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.TilCrt;

public interface TilCrtService {

    TilCrt createTilCrts(TilCrtDTO tilCrtDTO);
}
