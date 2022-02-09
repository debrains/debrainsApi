package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import com.debrains.debrainsApi.resource.TilResource;
import com.debrains.debrainsApi.service.TilService;
import com.debrains.debrainsApi.validator.TilValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tils", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilController {

    private final TilService tilService;

    private final TilRepository tilRepository;

    private final ModelMapper modelMapper;

    private final TilValidator tilValidator;

    /**
     * til 생성
     */
    @PostMapping
    public ResponseEntity createTil(@RequestBody @Validated TilDTO tilDTO) {
        tilValidator.validateDate(tilDTO);

        Til til = modelMapper.map(tilDTO, Til.class);
        til.totalCrtCount();
        Til newTil = tilRepository.save(til);
        var selfLinkBuilder = linkTo(TilController.class).slash(newTil.getId());

        EntityModel<Til> resource = EntityModel.of(newTil);
        URI createdUri = selfLinkBuilder.toUri();
        resource.add(linkTo(TilController.class).withSelfRel());
        resource.add(linkTo(TilController.class).withRel("query-tils"));
        resource.add(linkTo(methodOn(TilController.class).updateTil(newTil.getId(), tilDTO)).withRel("update-til"));
//        resource.add(selfLinkBuilder.withRel("update-til"));
        resource.add(Link.of("/docs/index.html#resources-tils-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    /**
     * til 리스트 조회
     */
    @GetMapping
    public ResponseEntity queryTil(Pageable pageable, PagedResourcesAssembler<Til> assembler) {

        Page<Til> page = tilRepository.findAll(pageable);
        var pagedResource = assembler.toModel(page, e -> new TilResource(e));
        pagedResource.add(Link.of("/docs/index.html#resources-tils-list").withRel("profile"));

        return ResponseEntity.ok(pagedResource);
    }

    /**
     * til 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity getTil(@PathVariable Long id) {
        Optional<Til> optionalTil = tilRepository.findById(id);
        if (optionalTil.isEmpty()) {
            throw new ApiException(ErrorCode.TIL_NOT_FOUND);
        }

        Til til = optionalTil.get();
        TilResource tilResource = new TilResource(til);
        tilResource.add(Link.of("/docs/index.html#resources-tils-get").withRel("profile"));
        return ResponseEntity.ok(tilResource);
    }

    /**
     * til 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity updateTil(@PathVariable Long id, @RequestBody TilDTO tilDTO) {
        Til savedTil = tilService.updateTil(id, tilDTO);

        TilResource tilResource = new TilResource(savedTil);
        tilResource.add(Link.of("/docs/index.html#resources-tils-update").withRel("profile"));

        return ResponseEntity.ok(tilResource);
    }

    /**
     * til 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTil(@PathVariable Long id) {
        Optional<Til> optionalTil = tilRepository.findById(id);
        if (optionalTil.isEmpty()) {
            throw new ApiException(ErrorCode.TIL_NOT_FOUND);
        }

        tilRepository.delete(optionalTil.get());
        TilResource tilResource = new TilResource(optionalTil.get());
        tilResource.add(Link.of("/docs/index.html#resources-tils-delete").withRel("profile"));

        return ResponseEntity.ok(tilResource);
    }
}
