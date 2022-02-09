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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilCrtServiceImpl implements TilCrtService {

    private final TilRepository tilRepository;

    private final TilCrtRepository tilCrtRepository;

    private final ModelMapper modelMapper;

    @Override
    public TilCrt createTilCrts(TilCrtDTO tilCrtDTO) {
        Optional<Til> optionalTil = tilRepository.findById(tilCrtDTO.getTilId());
        if (optionalTil.isEmpty()) {
            throw new ApiException(ErrorCode.TIL_NOT_FOUND);
        }

        Til til = optionalTil.get();

        TilCrt tilCrt = modelMapper.map(tilCrtDTO, TilCrt.class);
        System.out.println(tilCrt);
        til.addCrtCnt();

        return tilCrtRepository.save(tilCrt);
    }

}
