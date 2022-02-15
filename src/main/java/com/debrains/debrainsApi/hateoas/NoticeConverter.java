package com.debrains.debrainsApi.hateoas;

import com.debrains.debrainsApi.controller.SupportController;
import com.debrains.debrainsApi.controller.UserController;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.QnaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NoticeConverter implements RepresentationModelAssembler<NoticeDTO, EntityModel<NoticeDTO>> {
    @Override
    public EntityModel<NoticeDTO> toModel(NoticeDTO notice) {
        return EntityModel.of(notice,
                linkTo(methodOn(SupportController.class).getNotice(notice.getId())).withSelfRel());
    }
}
