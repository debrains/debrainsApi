package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilCurDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.TilRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.TilService;
import com.debrains.debrainsApi.util.PagedModelUtil;
import com.debrains.debrainsApi.validator.TilValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/tils", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilController {

    private final TilService tilService;

    private final TilRepository tilRepository;

    private final TilValidator tilValidator;

    /**
     * til 생성
     */
    @PostMapping
    public ResponseEntity createTil(@CurrentUser CustomUserDetails currentUser,
                                    @RequestBody @Validated TilDTO tilDTO) {
        tilValidator.validateDate(tilDTO);

        tilDTO.setUserId(currentUser.getId());

        TilDTO newTil = tilService.createTil(tilDTO);

        var selfLinkBuilder = linkTo(TilController.class).slash(newTil.getId());

        EntityModel<TilDTO> resource = EntityModel.of(newTil);
        URI createdUri = selfLinkBuilder.toUri();
        resource.add(linkTo(TilController.class).slash(newTil.getId()).withSelfRel());
        resource.add(linkTo(TilController.class).withRel("tils"));
        resource.add(linkTo(TilController.class).slash(newTil.getId()).withRel("update"));
        resource.add(Link.of("/docs/index.html#resources-tils-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    /**
     * til 리스트 조회
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TilDTO>>> queryTil(
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<TilDTO> assembler) {

        List<TilDTO> dto = tilService.getTilList(currentUser.getId(), pageable);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dto.size());
        Page<TilDTO> page = new PageImpl<>(dto.subList(start, end), pageable, dto.size());
        PagedModel<EntityModel<TilDTO>> resource = PagedModelUtil.getEntityModels(assembler, page,
                linkTo(TilController.class), TilDTO::getId);
        resource.add(Link.of("/docs/index.html#resources-tils-list").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity getTil(@CurrentUser CustomUserDetails currentUser, @PathVariable Long id) {
        TilDTO tilDTO = tilService.getTil(id);

        EntityModel<TilDTO> resource = EntityModel.of(tilDTO);
        resource.add(linkTo(TilController.class).slash(tilDTO.getId()).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-tils-get").withRel("profile"));
        if (tilDTO.getUserId() == currentUser.getId()) {
            resource.add(linkTo(TilController.class).slash(tilDTO.getId()).withRel("update"));
        }
        return ResponseEntity.ok(resource);
    }

    /**
     * til 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity updateTil(@CurrentUser CustomUserDetails currentUser,
                                    @PathVariable Long id,
                                    @RequestBody TilDTO tilDTO) {
        if (!currentUser.getId().equals(tilDTO.getUserId())) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }
        TilDTO dto = tilService.updateTil(id, tilDTO);

        EntityModel<TilDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(TilController.class).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-tils-update").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    /**
     * til 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTil(@CurrentUser CustomUserDetails currentUser, @PathVariable Long id) {
        Til til = tilRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TIL_NOT_FOUND));

        if (!currentUser.getId().equals(til.getUser().getId())) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }

        tilRepository.delete(til);

        return ResponseEntity.noContent().build();
    }

    /**
     * til 진행상황
     */
    @GetMapping("/current")
    public ResponseEntity currentTil(@CurrentUser CustomUserDetails currentUser) {
        TilCurDTO tilCurDTO = tilService.currentTil(currentUser.getId());
        return ResponseEntity.ok(tilCurDTO);
    }
}
