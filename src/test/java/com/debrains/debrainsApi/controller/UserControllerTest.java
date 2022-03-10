package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.RestDocsConfigurate;
import com.debrains.debrainsApi.common.WithAuthUser;
import com.debrains.debrainsApi.config.SecurityConfig;
import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.hateoas.QnaConverter;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.jwt.JwtAuthenticationFilter;
import com.debrains.debrainsApi.service.SupportService;
import com.debrains.debrainsApi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}))
@AutoConfigureRestDocs
@Import(RestDocsConfigurate.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SupportService supportService;
    @SpyBean
    private QnaConverter qnaConverter;

    @Test
    @DisplayName("유저정보 GET SUCCESS")
    @WithAuthUser
    void getUserInfoSuccess() throws Exception {
        // given
        Long id = 1L;
        UserInfoDTO dto = UserInfoDTO.builder()
                .id(1L)
                .email("dev@dev.com")
                .name("dev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl("https://devdev.github.io/")
                .snsUrl("https://www.instagram.com/devdev")
                .build();

        given(userService.getUserInfo(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/user/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(id)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.state").doesNotExist())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("get-userInfo",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("email").description("user unique email"),
                                fieldWithPath("name").description("user unique nickname"),
                                fieldWithPath("description").description("user description"),
                                fieldWithPath("img").description("profile image"),
                                fieldWithPath("githubUrl").description("github URL"),
                                fieldWithPath("blogUrl").description("blog URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("_links.self.href").description("link to self"))
                ));
        verify(userService).getUserInfo(any());
    }

    @Test
    @DisplayName("유저정보 저장")
    @WithAuthUser
    void saveUserInfo() throws Exception {
        // given
        UserInfoDTO dto = UserInfoDTO.builder()
                .id(1L)
                .email("dev@dev.com")
                .name("dev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl("https://devdev.github.io/")
                .snsUrl("https://www.instagram.com/devdev")
                .build();

        // when & then
        mvc.perform(patch("/user/info")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andDo(document("save-userInfo",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("email").description("user unique email"),
                                fieldWithPath("name").description("user unique nickname"),
                                fieldWithPath("description").description("user description"),
                                fieldWithPath("img").description("profile image"),
                                fieldWithPath("githubUrl").description("github URL"),
                                fieldWithPath("blogUrl").description("blog URL"),
                                fieldWithPath("snsUrl").description("SNS URL")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("email").description("user unique email"),
                                fieldWithPath("name").description("user unique nickname"),
                                fieldWithPath("description").description("user description"),
                                fieldWithPath("img").description("profile image"),
                                fieldWithPath("githubUrl").description("github URL"),
                                fieldWithPath("blogUrl").description("blog URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("_links.self.href").description("link to self")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("닉네임 중복")
    void validateName() throws Exception {
        // given
        String name = "test";
        Map<String, String> map = Map.of("name", name);
        given(userRepository.existsByName(name)).willReturn(true);

        // when & then
        mvc.perform(post("/user/validate")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
        verify(userRepository).existsByName(name);

    }

    @Test
    @WithAuthUser
    @DisplayName("프로필정도 GET SUCCESS")
    void getUserProfile() throws Exception {
        // given
        ProfileDTO dto = ProfileDTO.builder()
                .id(1L)
                .userId(1L)
                .purpose("취업")
                .build();
        given(userService.getProfile(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.purpose").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("get-userProfile",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("profile id number"),
                                fieldWithPath("userId").description("user id number"),
                                fieldWithPath("purpose").description("purpose"),
                                fieldWithPath("skills").description("skills"),
                                fieldWithPath("_links.self.href").description("link to self"))
                ));
        verify(userService).getProfile(any());
    }

    @Test
    @DisplayName("프로필정보 저장")
    @WithAuthUser
    void saveUserProfile() throws Exception {
        ProfileDTO dto = ProfileDTO.builder()
                .id(1L)
                .userId(1L)
                .purpose("취업")
                .build();

        // when & then
        mvc.perform(patch("/user/profile")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andDo(document("save-userProfile",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("userId").description("user id number"),
                                fieldWithPath("purpose").description("purpose"),
                                fieldWithPath("skills").description("skills")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("userId").description("user id number"),
                                fieldWithPath("purpose").description("purpose"),
                                fieldWithPath("skills").description("skills"),
                                fieldWithPath("_links.self.href").description("link to self")
                        )
                ));

    }

    @Test
    @WithAuthUser
    @DisplayName("유저보드 GET SUCCESS")
    void getUserBoard() throws Exception {
        Long id = 1L;
        UserBoardDTO board = UserBoardDTO.builder()
                .id(id)
                .email("dev@dev.com")
                .name("dev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl("https://devdev.github.io/")
                .snsUrl("https://www.instagram.com/devdev")
                .purpose("취업")
                .build();
        given(userService.getUserBoard(any())).willReturn(board);

        // when & then
        mvc.perform(get("/user/board/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.purpose").exists())
                .andDo(print())
                .andDo(document("get-userBoard",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("user id number"),
                                fieldWithPath("email").description("user unique email"),
                                fieldWithPath("name").description("user unique nickname"),
                                fieldWithPath("description").description("user description"),
                                fieldWithPath("img").description("profile image"),
                                fieldWithPath("githubUrl").description("github URL"),
                                fieldWithPath("blogUrl").description("blog URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("purpose").description("purpose"),
                                fieldWithPath("skills").description("skills"),
                                fieldWithPath("_links.self.href").description("link to self"))
                ));
        verify(userService).getUserBoard(any());
    }

    @Test
    @WithAuthUser
    @DisplayName("My QnA List 출력")
    void getMyQnaList() throws Exception {
        Long id = 1L;
        List<QnaDTO> qnaList = new ArrayList<>();

        for (long i=1L; i < 3L; i++) {
            QnaDTO qna = QnaDTO.builder()
                    .id(i)
                    .userId(id)
                    .title("Test Qna Title " + i)
                    .content("Test Qna Content " + i)
                    .completed(true)
                    .answer("Test Qna Answer From Admin " + i)
                    .build();
            qnaList.add(qna);
        }
        given(supportService.getQnaListByUserId(id)).willReturn(qnaList);


        // when & then
        mvc.perform(get("/user/qna")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$..userId").exists())
                .andExpect(jsonPath("$..title").exists())
                .andDo(print())
                .andDo(document("get-userQnaList",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.qnaDTOList[].id").description("qna id number"),
                                fieldWithPath("_embedded.qnaDTOList[].userId").description("user id number"),
                                fieldWithPath("_embedded.qnaDTOList[].title").description("qna title"),
                                fieldWithPath("_embedded.qnaDTOList[].content").description("qna content"),
                                fieldWithPath("_embedded.qnaDTOList[].completed").description("completed"),
                                fieldWithPath("_embedded.qnaDTOList[].answer").description("qna answer from admin"),
                                fieldWithPath("_embedded.qnaDTOList[]._links.self.href").description("link to self"),
                                fieldWithPath("_links.self.href").description("link to self")
                        )
                ));
        verify(supportService).getQnaListByUserId(id);
    }
}