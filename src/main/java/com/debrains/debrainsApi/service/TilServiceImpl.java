package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;

    @Override
    public Til updateTil(Long id, TilDTO tilDTO) {
        Til til = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        til.changeSubject(tilDTO.getSubject());
        til.changeDescription(tilDTO.getDescription());

        return tilRepository.save(til);
    }
}
