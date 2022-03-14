package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.AwsS3Uploader;
import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.TilCrt;
import com.debrains.debrainsApi.entity.TilCrtFile;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilCrtFileRepository;
import com.debrains.debrainsApi.repository.TilCrtRepository;
import com.debrains.debrainsApi.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TilCrtServiceImpl implements TilCrtService {

    private final TilRepository tilRepository;
    private final TilCrtRepository tilCrtRepository;
    private final TilCrtFileRepository fileRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ModelMapper modelMapper;

    private static String dirName = "tilcrt";

    @Override
    @Transactional
    public TilCrt createTilCrts(MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException {
        Til til = tilRepository.findById(tilCrtDTO.getTilId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));
        til.addCrtCnt();

        TilCrt entity = dtoToEntity(tilCrtDTO, til);
        TilCrt tilCrt = tilCrtRepository.save(entity);

        if (files != null) {
            if (!files[0].isEmpty()) {
                for (MultipartFile file : files) {
                    String path = awsS3Uploader.upload(file, dirName);

                    TilCrtFile tilCrtFile = TilCrtFile.builder()
                            .fileName(path.substring(path.indexOf(dirName)))
                            .originalName(file.getOriginalFilename())
                            .path(path)
                            .size(file.getSize())
                            .tilCrt(TilCrt.builder().id(tilCrt.getId()).build())
                            .build();
                    fileRepository.save(tilCrtFile);
                }
            }
        }

        return tilCrt;
    }

    @Override
    @Transactional
    public TilCrt updateTilCrt(Long id, MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        tilCrt.changeTilCrt(tilCrtDTO);

        if (!files[0].isEmpty()) {
            for (MultipartFile file : files) {
                String path = awsS3Uploader.upload(file, dirName);

                TilCrtFile tilCrtFile = TilCrtFile.builder()
                        .fileName(path.substring(path.indexOf(dirName)))
                        .originalName(file.getOriginalFilename())
                        .path(path)
                        .size(file.getSize())
                        .tilCrt(TilCrt.builder().id(id).build())
                        .build();
                fileRepository.save(tilCrtFile);
            }
        }

        return tilCrt;
    }

    @Override
    public void deleteTilCrt(TilCrt tilCrt) {
        Til til = tilRepository.findById(tilCrt.getTil().getId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        tilCrtRepository.delete(tilCrt);
        til.removeCrtCnt();
    }

    @Override
    public void deleteTilCrtFile(Long id) {
        TilCrtFile file = fileRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_FILE));
        awsS3Uploader.delete(file.getFileName());
    }

}
