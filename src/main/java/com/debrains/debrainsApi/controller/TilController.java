package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.repository.TilRepository;
import com.debrains.debrainsApi.validator.TilValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
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
@RequestMapping(value = "/api/tils", produces = MediaTypes.HAL_JSON_VALUE)
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
        URI createdUri = linkTo(TilController.class).slash(newTil.getId()).toUri();

        return ResponseEntity.created(createdUri).body(til);
    }

}
