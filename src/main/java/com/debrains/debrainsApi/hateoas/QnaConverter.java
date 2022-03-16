package com.debrains.debrainsApi.hateoas;

import com.debrains.debrainsApi.controller.SupportController;
import com.debrains.debrainsApi.dto.QnaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class QnaConverter implements RepresentationModelAssembler<QnaDTO, EntityModel<QnaDTO>> {
    @Override
    public EntityModel<QnaDTO> toModel(QnaDTO qna) {
        return EntityModel.of(qna,
                linkTo(methodOn(SupportController.class).getQna(null, qna.getId())).withSelfRel());
    }
}
