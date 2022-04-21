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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TilDTO createTil(TilDTO tilDTO) {

        Til til = dtoToEntity(tilDTO);
        til.totalCrtCount();
        Til newTil = tilRepository.save(til);

        return modelMapper.map(newTil, TilDTO.class);
    }

    @Override
    @Transactional
    public TilDTO updateTil(Long id, TilDTO tilDTO) {
        Til til = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        til.changeTil(tilDTO);

        return modelMapper.map(til, TilDTO.class);
    }

    @Override
    @Transactional
    public List<TilDTO> getTilList(Long userId, Pageable pageable) {
        System.out.println();
        List<TilDTO> dtoList = tilRepository.findByUserId(userId, pageable)
                .stream().map(entity -> modelMapper.map(entity, TilDTO.class))
                .collect(Collectors.toList());

        for (TilDTO dto : dtoList) {
            Til til = modelMapper.map(dto, Til.class);
            til.expiredCheck();
        }

        return dtoList;
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
        Til entity = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));
        TilDTO dto = modelMapper.map(entity, TilDTO.class);
        return dto;
    }
}
