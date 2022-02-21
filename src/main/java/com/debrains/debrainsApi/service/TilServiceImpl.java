package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilServiceImpl implements TilService {

    private final TilRepository tilRepository;

    @Override
    public Til updateTil(Long id, TilDTO tilDTO) {
        Optional<Til> optionalTil = tilRepository.findById(id);

        Til til = optionalTil.get();
        til.changeSubject(tilDTO.getSubject());
        til.changeDescription(tilDTO.getDescription());

        return tilRepository.save(til);
    }
}
