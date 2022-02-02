package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;

    private final ModelMapper modelMapper;

    @Override
    public TilDTO createTil(TilDTO tilDTO) {
        Til til = modelMapper.map(tilDTO, Til.class);
        til.totalCrtCount();
        Til newTil = tilRepository.save(til);

        return tilDTO;
    }

    @Override
    public Til updateTil(Long id, TilDTO tilDTO) {
        Optional<Til> optionalTil = tilRepository.findById(id);
        if (optionalTil.isEmpty()) {
            throw new ApiException(ErrorCode.TIL_NOT_FOUND);
        }

        Til til = optionalTil.get();
        til.changeSubject(tilDTO.getSubject());
        til.changeDescription(tilDTO.getDescription());

        return tilRepository.save(til);
    }
}
