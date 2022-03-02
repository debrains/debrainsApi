package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.TilCrt;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilCrtRepository;
import com.debrains.debrainsApi.service.TilCrtService;
import com.debrains.debrainsApi.util.PagedModelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/til-crts", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilCrtController {

    private final TilCrtService tilCrtService;

    private final TilCrtRepository tilCrtRepository;

    /**
     * til 인증 생성
     */
    @PostMapping
    public ResponseEntity createTilCrts(@RequestPart(value = "files", required = false) MultipartFile files,
                                        @RequestPart(value = "tilCrtDTO") @Validated TilCrtDTO tilCrtDTO)
            throws IllegalStateException, IOException {

        TilCrt newTilCrt = tilCrtService.createTilCrts(tilCrtDTO);
        var selfLinkBuilder = linkTo(TilCrtController.class).slash(newTilCrt.getId());

        EntityModel<TilCrt> resource = EntityModel.of(newTilCrt);
        URI createdUri = selfLinkBuilder.toUri();
        resource.add(linkTo(TilCrtController.class).slash(newTilCrt.getId()).withSelfRel());
        resource.add(linkTo(TilCrtController.class).withRel("query-til-crts"));
        resource.add(selfLinkBuilder.withRel("update-til-crt"));
        resource.add(Link.of("/docs/index.html#resources-til-crts-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    /**
     * til 인증 리스트 조회
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TilCrt>>> queryTilCrts(Pageable pageable,
                                                                        PagedResourcesAssembler<TilCrt> assembler) {

        Page<TilCrt> page = tilCrtRepository.findAll(pageable);

        PagedModel<EntityModel<TilCrt>> resource = PagedModelUtil.getEntityModels(assembler, page,
                linkTo(TilCrtController.class), TilCrt::getId);
        resource.add(Link.of("/docs/index.html#resources-tils-list").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity getTilCrt(@PathVariable Long id) {

        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        EntityModel<TilCrt> resource = EntityModel.of(tilCrt);
        resource.add(linkTo(TilCrtController.class).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-til-crts-get").withRel("profile"));
        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity updateTilCrt(@PathVariable Long id,
                                       @RequestPart(value = "files", required = false) MultipartFile files,
                                       @RequestPart(value = "tilCrtDTO") @Validated TilCrtDTO tilCrtDTO) {

        TilCrt savedTilCrt = tilCrtService.updateTilCrt(id, tilCrtDTO);

        EntityModel<TilCrt> resource = EntityModel.of(savedTilCrt);
        resource.add(linkTo(TilCrtController.class).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-til-crts-update").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 인증 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTilCrt(@PathVariable Long id) {
        TilCrt tilCrt = tilCrtRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TILCRT_NOT_FOUND));

        tilCrtService.deleteTilCrt(tilCrt);
        EntityModel<TilCrt> resource = EntityModel.of(tilCrt);
        resource.add(linkTo(TilCrtController.class).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-til-crts-delete").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

}
