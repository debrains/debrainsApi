package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.*;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.hateoas.EventConverter;
import com.debrains.debrainsApi.hateoas.NoticeConverter;
import com.debrains.debrainsApi.hateoas.QnaConverter;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/support", produces = MediaTypes.HAL_JSON_VALUE)
public class SupportController {

    private final SupportService supportService;
    private final NoticeConverter noticeConverter;
    private final EventConverter eventConverter;
    private final QnaConverter qnaConverter;

    @GetMapping("/notice")
    public ResponseEntity getNoticeList() {
        List<EntityModel<NoticeDTO>> noticeList = supportService.getNoticeList()
                .stream().map(noticeDTO -> noticeConverter.toModel(noticeDTO)).collect(Collectors.toList());
        CollectionModel<EntityModel<NoticeDTO>> resource = CollectionModel.of(noticeList,
                linkTo(methodOn(this.getClass()).getNoticeList()).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/notice/{id}")
    public ResponseEntity getNotice(@PathVariable Long id) {
        NoticeDTO notice = supportService.getNotice(id);
        EntityModel<NoticeDTO> resource = noticeConverter.toModel(notice);
        resource.add(linkTo(methodOn(this.getClass()).getNoticeList()).withRel("list"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/event")
    public ResponseEntity getEventList() {
        List<EntityModel<EventDTO>> eventList = supportService.getEventList()
                .stream().map(eventDTO -> eventConverter.toModel(eventDTO)).collect(Collectors.toList());
        CollectionModel<EntityModel<EventDTO>> resource = CollectionModel.of(eventList,
                linkTo(methodOn(this.getClass()).getEventList()).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity getEvent(@PathVariable Long id) {
        EventDTO event = supportService.getEvent(id);
        EntityModel<EventDTO> resource = eventConverter.toModel(event);
        resource.add(linkTo(methodOn(this.getClass()).getEventList()).withRel("list"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/qna/{id}")
    public ResponseEntity getQna(@CurrentUser CustomUserDetails user, @PathVariable Long id) {
        QnaDTO qna = supportService.getQna(id);

        if (user.getId() != qna.getUserId()) {
            throw new ApiException(ErrorCode.NO_AUTHENTICATION);
        }

        EntityModel<QnaDTO> resource = qnaConverter.toModel(qna);

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/qna")
    public ResponseEntity saveQna(@CurrentUser CustomUserDetails user, @RequestBody @Validated QnaFormDTO qna) {
        qna.setUserId(user.getId());
        QnaDTO entity = supportService.saveQna(qna);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(methodOn(this.getClass()).getQna(null, entity.getId()));
        URI createdUri = selfLinkBuilder.toUri();
        EntityModel<QnaDTO> resource = EntityModel.of(entity);
        resource.add(linkTo(methodOn(this.getClass()).saveQna(null, qna)).withSelfRel());
        resource.add(selfLinkBuilder.withRel("create"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    @PostMapping("/skill")
    public ResponseEntity requestSkill(@RequestBody @Validated SkillReqDTO dto) {
        supportService.saveSkillReq(dto);
        return ResponseEntity.ok().build();
    }
}
