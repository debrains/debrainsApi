package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.TilCrt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TilCrtService {

    TilCrt createTilCrts(MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException;

    TilCrt updateTilCrt(Long id, MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException;

    void deleteTilCrt(TilCrt tilCrt);

    void deleteTilCrtFile(Long id);
}
