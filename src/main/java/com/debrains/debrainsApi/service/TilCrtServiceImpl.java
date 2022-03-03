package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.TilCrt;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilCrtRepository;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilCrtServiceImpl implements TilCrtService {

    private final TilRepository tilRepository;

    private final TilCrtRepository tilCrtRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TilCrt createTilCrts(TilCrtDTO tilCrtDTO) {
        Til til = tilRepository.findById(tilCrtDTO.getTilId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        til.addCrtCnt();

        TilCrt tilCrt = modelMapper.map(tilCrtDTO, TilCrt.class);

        return tilCrtRepository.save(tilCrt);
    }

    @Override
    public TilCrt updateTilCrt(Long id, TilCrtDTO tilCrtDTO) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        tilCrt.changeTilCrt(tilCrtDTO);

        return tilCrtRepository.save(tilCrt);
    }

    @Override
    public void deleteTilCrt(TilCrt tilCrt) {
        Optional<Til> optionalTil = tilRepository.findById(tilCrt.getTil().getId());
        Til til = optionalTil.get();

        tilCrtRepository.delete(tilCrt);

        til.removeCrtCnt();
    }

}
