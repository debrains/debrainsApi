package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;
    private final ModelMapper modelMapper;

    @Override
    public Til updateTil(Long id, TilDTO tilDTO) {
        Til til = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        til.changeSubject(tilDTO.getSubject());
        til.changeDescription(tilDTO.getDescription());

        return tilRepository.save(til);
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
