package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.entity.TilCrt;
import com.debrains.debrainsApi.service.TilCrtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/til-crts", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilCrtController {

    private final TilCrtService tilCrtService;

    @PostMapping
    public ResponseEntity createTilCrts(@RequestBody @Validated TilCrtDTO tilCrtDTO) {

        TilCrt newTilCrt = tilCrtService.createTilCrts(tilCrtDTO);
        var selfLinkBuilder = linkTo(TilCrtController.class).slash(newTilCrt.getId());

        EntityModel<TilCrt> resource = EntityModel.of(newTilCrt);
        URI createdUri = selfLinkBuilder.toUri();
        resource.add(linkTo(TilCrtController.class).withSelfRel());
        resource.add(linkTo(TilCrtController.class).withRel("query-til-crts"));
        resource.add(selfLinkBuilder.withRel("update-til-crt"));
        resource.add(Link.of("/docs/index.html#resources-til-crts-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }
}
