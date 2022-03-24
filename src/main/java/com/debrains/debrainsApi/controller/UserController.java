package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.hateoas.QnaConverter;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.SupportService;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        resource.add(Link.of("/docs/index.html#resources-user-get").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/info")
    public ResponseEntity saveUserInfo(@RequestParam(value = "photo", required = false) MultipartFile img,
                                       @CurrentUser CustomUserDetails user,
                                       @RequestBody @Validated UserInfoDTO dto) throws IOException {
        if (dto.getId() != user.getId()) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }
        userService.updateUserInfo(img, dto);

        EntityModel<UserInfoDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(this.getClass()).slash("/info").withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-user-update").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/validate")
    public ResponseEntity validateName(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        Map<String, Boolean> result = Map.of("exist", userRepository.existsByName(name));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile")
    public ResponseEntity getProfile(@CurrentUser CustomUserDetails user) {
        ProfileDTO userProfile = userService.getProfile(user.getId());
        EntityModel<ProfileDTO> resource = EntityModel.of(userProfile);
        resource.add(linkTo(methodOn(this.getClass()).getProfile(user)).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-user-profile-get").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/profile")
    public ResponseEntity saveProfile(@CurrentUser CustomUserDetails user,
                                      @RequestBody @Validated ProfileDTO dto) {
        if (dto.getUserId() != user.getId()) {
            throw new ApiException(ErrorCode.NO_AUTHORIZATION);
        }
        userService.updateProfile(dto);

        EntityModel<ProfileDTO> resource = EntityModel.of(dto);
        resource.add(linkTo(methodOn(this.getClass()).saveProfile(null, dto)).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-user-profile-update").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity getUserBoard(@PathVariable Long id) {
        UserBoardDTO user = userService.getUserBoard(id);
        EntityModel<UserBoardDTO> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(this.getClass()).getUserBoard(id)).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-user-board-get").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/qna")
    public ResponseEntity getMyQnaList(@CurrentUser CustomUserDetails user) {
        List<EntityModel<QnaDTO>> qnaList = supportService.getQnaListByUserId(user.getId())
                .stream().map(qnaDTO -> qnaConverter.toModel(qnaDTO)).collect(Collectors.toList());
        CollectionModel<EntityModel<QnaDTO>> resource = CollectionModel.of(qnaList,
                linkTo(methodOn(this.getClass()).getMyQnaList(user)).withSelfRel());
        resource.add(Link.of("/docs/index.html#resources-user-qnaList-get").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

}
