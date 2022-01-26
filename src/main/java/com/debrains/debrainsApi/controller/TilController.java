package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.TilResource;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.repository.TilRepository;
import com.debrains.debrainsApi.validator.TilValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/tils", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TilController {

    private final TilRepository tilRepository;

    private final ModelMapper modelMapper;

    private final TilValidator tilValidator;

    @PostMapping
    public ResponseEntity createTil(@RequestBody @Validated TilDTO tilDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        tilValidator.validate(tilDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Til til = modelMapper.map(tilDTO, Til.class);
        til.totalCrtCount();
        Til newTil = tilRepository.save(til);
        var selfLinkBuilder = linkTo(TilController.class).slash(newTil.getId());
        URI createdUri = selfLinkBuilder.toUri();
        TilResource tilResource = new TilResource(til);
        tilResource.add(linkTo(TilController.class).withRel("query-tils"));
        tilResource.add(selfLinkBuilder.withRel("update-til"));
        tilResource.add(Link.of("/docs/index.html#resources-tils-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(tilResource);
    }

}
