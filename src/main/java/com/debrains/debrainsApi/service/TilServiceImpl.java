package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;

    @Override
    @Transactional
    public Til createTil(TilDTO tilDTO) {

        Til til = dtoToEntity(tilDTO);
        log.info("Til: " + til);
        til.totalCrtCount();
        Til newTil = tilRepository.save(til);

        return newTil;
    }

    @Override
    @Transactional
    public Til updateTil(Long id, TilDTO tilDTO) {
        Til til = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        til.changeTil(tilDTO);

        return til;
    }
}
