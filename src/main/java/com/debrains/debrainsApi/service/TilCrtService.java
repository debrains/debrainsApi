package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.TilCrt;
import org.springframework.web.multipart.MultipartFile;

public interface TilCrtService {

    TilCrt createTilCrts(TilCrtDTO tilCrtDTO, MultipartFile files);
}
