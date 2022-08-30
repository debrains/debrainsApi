package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.AwsS3Uploader;
import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.dto.TilCrtFileDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TilCrtServiceImpl implements TilCrtService {

    private final TilRepository tilRepository;
    private final TilCrtRepository tilCrtRepository;
    private final TilCrtFileRepository tilCrtFileRepository;
    private final TilCrtFileRepository fileRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ModelMapper modelMapper;

    private static String dirName = "tilcrt";

    @Override
    @Transactional
    public TilCrtDTO createTilCrts(MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException {
        Til til = tilRepository.findById(tilCrtDTO.getTilId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));
        til.addCrtCnt();

        TilCrt entity = dtoToEntity(tilCrtDTO, til);
        TilCrt tilCrt = tilCrtRepository.save(entity);

        if (files != null && !files[0].isEmpty()) {
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

        return modelMapper.map(tilCrt, TilCrtDTO.class);
    }

    @Override
    @Transactional
    public TilCrtDTO updateTilCrt(Long id, MultipartFile[] files, TilCrtDTO tilCrtDTO) throws IOException {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        tilCrt.changeTilCrt(tilCrtDTO);

        if (files != null && !files[0].isEmpty()) {
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

        TilCrtDTO dto = modelMapper.map(tilCrt, TilCrtDTO.class);

        List<TilCrtFileDTO> fileList = fileRepository.findByTilCrtId(id)
                .stream().map(file -> modelMapper.map(file, TilCrtFileDTO.class)).collect(Collectors.toList());

        dto.setFileList(fileList);

        return dto;
    }

    @Override
    @Transactional
    public void deleteTilCrt(TilCrt tilCrt) {
        Til til = tilRepository.findById(tilCrt.getTil().getId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));
        til.removeCrtCnt();

        tilCrtRepository.delete(tilCrt);
    }

    @Override
    public void deleteTilCrtFile(Long id) {
        TilCrtFile file = fileRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_FILE));
        awsS3Uploader.delete(file.getFileName());
        tilCrtFileRepository.delete(file);
    }

    @Override
    public List<TilCrtDTO> getTilCrtById(Long id) {
        List<TilCrtDTO> tilcrtList = tilCrtRepository.findAllById(id)
                .stream().map(tilCrt -> modelMapper.map(tilCrt, TilCrtDTO.class)).collect(Collectors.toList());
        return tilcrtList;
    }

    @Override
    public Page<TilCrtDTO> getAdminTilcrtList(Pageable pageable) {
        Page<TilCrtDTO> tilcrtList = tilCrtRepository.findAll(pageable)
                .map(tilcrt -> modelMapper.map(tilcrt, TilCrtDTO.class));
        return tilcrtList;
    }

    @Override
    public TilCrtDTO getTilcrt(Long id) {
        TilCrt entity = tilCrtRepository.findById(id).orElseThrow();
        TilCrtDTO dto = modelMapper.map(entity, TilCrtDTO.class);
        return dto;
    }

    @Override
    public List<TilCrtFileDTO> getTilcrtFiles(Long id) {
        List<TilCrtFileDTO> files = fileRepository.findByTilCrtId(id)
                .stream().map(file -> modelMapper.map(file, TilCrtFileDTO.class)).collect(Collectors.toList());
        return files;
    }

    @Override
    public TilCrtFileDTO getTilCrtFileById(Long id) {
        TilCrtFile file = fileRepository.findById(id).orElseThrow();
        TilCrtFileDTO dto = modelMapper.map(file, TilCrtFileDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void updateAdminTilCrt(TilCrtDTO tilcrt) {
        TilCrt entity = tilCrtRepository.findById(tilcrt.getId()).orElseThrow();
        entity.updateAdminTilCrt(tilcrt);
    }

    @Override
    public List<TilCrtDTO> tilCrtList(Long userId, Long tilId, Pageable pageable) {
        List<TilCrtDTO> dtoList = tilCrtRepository.findByUserIdAndTilId(userId, tilId, pageable)
                .stream().map(TilCrtDTO::new)
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public TilCrtDTO getTilCrt(Long id) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        TilCrtDTO dto = modelMapper.map(tilCrt, TilCrtDTO.class);

        List<TilCrtFileDTO> fileList = fileRepository.findByTilCrtId(dto.getId())
                .stream().map(file -> modelMapper.map(file, TilCrtFileDTO.class)).collect(Collectors.toList());

        dto.setFileList(fileList);

        return dto;
    }

}
