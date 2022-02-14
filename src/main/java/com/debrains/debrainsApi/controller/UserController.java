package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.hateoas.QnaConverter;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.SupportService;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = MediaTypes.HAL_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SupportService supportService;
    private final QnaConverter qnaConverter;

    @GetMapping("/info")
    public ResponseEntity getUserInfo(@CurrentUser CustomUserDetails user) {
        UserInfoDTO userInfo = userService.getUserInfo(user.getId());
        EntityModel<UserInfoDTO> resource = EntityModel.of(userInfo);
        resource.add(linkTo(methodOn(this.getClass()).getUserInfo(user)).withSelfRel());
//        resource.add(linkTo(this.getClass()).slash(userInfo.getId()).withRel("/user-update"));

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/info")
    public ResponseEntity saveUserInfo(@RequestBody @Validated UserInfoDTO dto) {
        userService.updateUserInfo(dto);
        EntityModel<UserInfoDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(methodOn(this.getClass()).saveUserInfo(dto)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/validateName")
    public boolean validateName(@RequestBody String name) {
        return userRepository.existsByName(name);
    }

    @GetMapping("/profile")
    public ResponseEntity getProfile(@CurrentUser CustomUserDetails user) {
        ProfileDTO userProfile = userService.getProfile(user.getId());
        EntityModel<ProfileDTO> resource = EntityModel.of(userProfile);
        resource.add(linkTo(methodOn(this.getClass()).getProfile(user)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/profile")
    public ResponseEntity saveProfile(@RequestBody @Validated ProfileDTO dto) {
        userService.updateProfile(dto);
        EntityModel<ProfileDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(methodOn(this.getClass()).saveProfile(dto)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity getUserBoard(@PathVariable Long id) {
        UserBoardDTO user = userService.getUserBoard(id);
        EntityModel<UserBoardDTO> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(this.getClass()).getUserBoard(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/qna")
    public ResponseEntity getMyQnaList(@CurrentUser CustomUserDetails user) {
        List<EntityModel<QnaDTO>> qnaList = supportService.getQnaListByUserId(user.getId())
                .stream().map(qnaDTO -> qnaConverter.toModel(qnaDTO)).collect(Collectors.toList());
        CollectionModel<EntityModel<QnaDTO>> resource = CollectionModel.of(qnaList,
                linkTo(methodOn(this.getClass()).getMyQnaList(user)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

}
