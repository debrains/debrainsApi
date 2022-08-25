package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.entity.TilCrt;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilCrtRepository;
import com.debrains.debrainsApi.repository.TilRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.TilCrtService;
import com.debrains.debrainsApi.util.PagedModelUtil;
import com.debrains.debrainsApi.validator.TilCrtValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/til-crts", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilCrtController {

    private final TilRepository tilRepository;

    private final TilCrtService tilCrtService;

    private final TilCrtRepository tilCrtRepository;

    private final TilCrtValidator tilCrtValidator;

    /**
     * til 인증 생성
     */
    @PostMapping
    public ResponseEntity createTilCrts(@CurrentUser CustomUserDetails currentUser,
                                        @RequestPart(value = "files", required = false) MultipartFile[] files,
                                        @RequestPart(value = "tilCrtDTO") @Validated TilCrtDTO tilCrtDTO)
            throws IOException {
        Til til = tilRepository.findById(tilCrtDTO.getTilId())
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));
        if (!currentUser.getId().equals(til.getUser().getId())) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }

        tilCrtValidator.validateDate(tilCrtDTO);
        tilCrtValidator.validateCrt(tilCrtDTO, files);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Long tilCrtCount = tilCrtRepository.tilCrtCount(currentUser.getId(), tilCrtDTO.getTilId(), start, end);
        if (tilCrtCount > 0) {
            throw new ApiException(ErrorCode.TILCRT_TODAY);
        }

        tilCrtDTO.setUserId(currentUser.getId());
        TilCrtDTO newTilCrt = tilCrtService.createTilCrts(files, tilCrtDTO);
        var selfLinkBuilder = linkTo(TilCrtController.class).slash(newTilCrt.getId());

        EntityModel<TilCrtDTO> resource = EntityModel.of(newTilCrt);
        URI createdUri = selfLinkBuilder.toUri();
        resource.add(linkTo(TilCrtController.class).slash(newTilCrt.getId()).withSelfRel());
        resource.add(linkTo(TilCrtController.class).withRel("til-crts"));
        resource.add(selfLinkBuilder.withRel("update"));
        resource.add(Link.of("/docs/index.html#resources-til-crts-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    /**
     * til 인증 리스트 조회
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TilCrtDTO>>> queryTilCrts(
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<TilCrtDTO> assembler, @RequestParam(value = "tilId") Long tilId) {

        Page<TilCrtDTO> page = tilCrtService.tilCrtList(currentUser.getId(), tilId, pageable);

        PagedModel<EntityModel<TilCrtDTO>> resource = PagedModelUtil
                .getEntityModels(assembler, page, linkTo(TilCrtController.class), TilCrtDTO::getId);

        resource.add(Link.of("/docs/index.html#resources-til-crts-list").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity getTilCrt(@CurrentUser CustomUserDetails currentUser, @PathVariable Long id) {
        tilCrtUserCheck(currentUser, id);

        TilCrtDTO tilCrtDTO = tilCrtService.getTilCrt(id);

        EntityModel<TilCrtDTO> resource = EntityModel.of(tilCrtDTO);
        resource.add(linkTo(TilCrtController.class).slash(tilCrtDTO.getId()).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-til-crts-get").withRel("profile"));
        if (tilCrtDTO.getUserId() == currentUser.getId()) {
            resource.add(linkTo(TilCrtController.class).slash(tilCrtDTO.getId()).withRel("update"));
        }
        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity updateTilCrt(@CurrentUser CustomUserDetails currentUser,
                                       @PathVariable Long id,
                                       @RequestPart(value = "files", required = false) MultipartFile[] files,
                                       @RequestPart(value = "tilCrtDTO") @Validated TilCrtDTO tilCrtDTO) throws IOException {
        tilCrtUserCheck(currentUser, id);

        TilCrtDTO dto = tilCrtService.updateTilCrt(id, files, tilCrtDTO);

        EntityModel<TilCrtDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(TilCrtController.class).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-til-crts-update").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTilCrt(@CurrentUser CustomUserDetails currentUser,
                                       @PathVariable Long id) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        if (!currentUser.getId().equals(tilCrt.getUser().getId())) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }

        tilCrtService.deleteTilCrt(tilCrt);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/file")
    public ResponseEntity deleteTilCrtFile(@CurrentUser CustomUserDetails user,
                                           @PathVariable Long id, @RequestParam(value = "fileId") Long fileId) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        if (tilCrt.getUser().getId() != user.getId()) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }

        tilCrtService.deleteTilCrtFile(fileId);

        return ResponseEntity.noContent().build();
    }

    private void tilCrtUserCheck(@CurrentUser CustomUserDetails currentUser, @PathVariable Long id) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));
        if (!currentUser.getId().equals(tilCrt.getUser().getId())) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }
    }
}
