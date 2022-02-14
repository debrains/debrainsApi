package com.debrains.debrainsApi.hateoas;

import com.debrains.debrainsApi.controller.SupportController;
import com.debrains.debrainsApi.dto.EventDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EventConverter implements RepresentationModelAssembler<EventDTO, EntityModel<EventDTO>> {
    @Override
    public EntityModel<EventDTO> toModel(EventDTO event) {
        return EntityModel.of(event,
                linkTo(methodOn(SupportController.class).getEvent(event.getId())).withSelfRel());
    }
}
