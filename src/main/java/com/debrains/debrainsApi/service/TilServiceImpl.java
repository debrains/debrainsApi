package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCurDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Til createTil(TilDTO tilDTO) {

        Til til = dtoToEntity(tilDTO);
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

    @Override
    @Transactional
    public Page<Til> getTilList(Pageable pageable) {
        Page<Til> page = tilRepository.findAll(pageable);
        List<Til> tils = page.getContent();
        for (Til til : tils) {
            til.expiredCheck();
        }

        return page;
    }

    @Override
    public TilCurDTO currentTil(Long userId) {
        Long totalTil = tilRepository.totalTil(userId);
        Long ingTil = tilRepository.ingTil(userId);
        Long succTil = tilRepository.succTil(userId);
        Long failTil = tilRepository.failTil(userId);

        TilCurDTO tilCurDTO = TilCurDTO.builder()
                .totalCnt(totalTil)
                .ingCnt(ingTil)
                .succCnt(succTil)
                .failCnt(failTil)
                .build();
        return tilCurDTO;
    }

    @Override
    public Page<TilDTO> getAdminTilList(Pageable pageable) {
        Page<TilDTO> tilList = tilRepository.findAll(pageable)
                .map(til -> modelMapper.map(til, TilDTO.class));
        return tilList;
    }

    @Override
    public TilDTO getTil(Long id) {
        Til entity = tilRepository.findById(id).orElseThrow();
        TilDTO dto = modelMapper.map(entity, TilDTO.class);
        return dto;
    }
}
