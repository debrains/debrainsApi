package com.debrains.debrainsApi.resource;

import com.debrains.debrainsApi.controller.TilController;
import com.debrains.debrainsApi.entity.Til;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class TilResource extends EntityModel<Til> {

    public TilResource(Til til, Link... links){
        super(til, links);
        add(linkTo(TilController.class).slash(til.getId()).withSelfRel());
    }
}
