package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.FileDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilCrtServiceImpl implements TilCrtService {

    private final TilRepository tilRepository;

    private final TilCrtRepository tilCrtRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TilCrt createTilCrts(TilCrtDTO tilCrtDTO, MultipartFile files) {
        Optional<Til> optionalTil = tilRepository.findById(tilCrtDTO.getTilId());
        if (optionalTil.isEmpty()) {
            throw new ApiException(ErrorCode.TIL_NOT_FOUND);
        }

        Til til = optionalTil.get();
        til.addCrtCnt();

        FileDTO fileDTO = fileService.store(files);

        TilCrt tilCrt = modelMapper.map(tilCrtDTO, TilCrt.class);

        tilCrt.createFile(fileDTO);

        return tilCrtRepository.save(tilCrt);
    }

}
