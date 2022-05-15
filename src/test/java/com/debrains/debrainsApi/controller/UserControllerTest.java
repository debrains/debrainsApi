package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.config.RestDocsConfigurate;
import com.debrains.debrainsApi.config.WithAuthUser;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                .id(id)
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
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.state").doesNotExist())
                .andDo(print())
                .andDo(document("get-userInfo",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저고유 ID"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("닉네임"),
                                fieldWithPath("description").description("소개글"),
                                fieldWithPath("img").description("프로필사진"),
                                fieldWithPath("githubUrl").description("깃허브 URL"),
                                fieldWithPath("blogUrl").description("블로그 URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("consent").description("정보제공동의"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
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
        MockMultipartFile form = new MockMultipartFile("userInfoDTO", "userInfoDTO",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(dto).getBytes());
        MockMultipartFile image = new MockMultipartFile("photo", "image.png",
                MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        // when & then
        mvc.perform(multipart("/user/info")
                        .file(form)
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                            })
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andDo(document("save-userInfo",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestPartFields(
                                "userInfoDTO",
                                fieldWithPath("id").description("유저고유 ID"),
                                fieldWithPath("email").description("이메일 (수정불가)"),
                                fieldWithPath("name").description("닉네임 (중복불가)"),
                                fieldWithPath("description").description("소개글"),
                                fieldWithPath("img").description("프로필사진"),
                                fieldWithPath("githubUrl").description("깃허브 URL"),
                                fieldWithPath("blogUrl").description("블로그 URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("consent").description("정보제공동의")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저고유 ID"),
                                fieldWithPath("email").description("이메일 (수정불가)"),
                                fieldWithPath("name").description("닉네임 (중복불가)"),
                                fieldWithPath("description").description("소개글"),
                                fieldWithPath("img").description("프로필사진"),
                                fieldWithPath("githubUrl").description("깃허브 URL"),
                                fieldWithPath("blogUrl").description("블로그 URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("consent").description("정보제공동의"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
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
                .andExpect(jsonPath("$.exist").value(true))
                .andDo(print())
                .andDo(document("validate-name",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("신청 닉네임")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("exist").type(JsonFieldType.BOOLEAN).description("닉네임 중복여부")
                        )
                ));
        verify(userRepository).existsByName(name);

    }

    @Test
    @WithAuthUser
    @DisplayName("프로필정보 GET SUCCESS")
    void getUserProfile() throws Exception {
        // given
        ProfileDTO dto = ProfileDTO.builder()
                .id(1L)
                .userId(1L)
                .purpose("취업")
                .skills(List.of("1", "3"))
                .build();
        given(userService.getProfile(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.purpose").value("취업"))
                .andExpect(jsonPath("$.skills").exists())
                .andDo(print())
                .andDo(document("get-userProfile",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("프로필 ID"),
                                fieldWithPath("userId").description("유저고유 ID"),
                                fieldWithPath("purpose").description("목적"),
                                fieldWithPath("skills").type(JsonFieldType.ARRAY).description("관심 스킬리스트"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
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
                .skills(List.of("1", "3"))
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
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("id").description("프로필 ID"),
                                fieldWithPath("userId").description("유저고유 ID"),
                                fieldWithPath("purpose").description("목적"),
                                fieldWithPath("skills").type(JsonFieldType.ARRAY).description("관심 스킬리스트")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("프로필 ID"),
                                fieldWithPath("userId").description("유저고유 ID"),
                                fieldWithPath("purpose").description("목적"),
                                fieldWithPath("skills").type(JsonFieldType.ARRAY).description("관심 스킬리스트"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
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
                .skills(List.of("1", "3"))
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
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저고유 ID"),
                                fieldWithPath("email").description("이메일 (중복불가)"),
                                fieldWithPath("name").description("닉네임 (중복불가)"),
                                fieldWithPath("description").description("소개글"),
                                fieldWithPath("img").description("프로필사진"),
                                fieldWithPath("githubUrl").description("깃허브 URL"),
                                fieldWithPath("blogUrl").description("블로그 URL"),
                                fieldWithPath("snsUrl").description("SNS URL"),
                                fieldWithPath("purpose").description("목적"),
                                fieldWithPath("skills").type(JsonFieldType.ARRAY).description("관심 스킬리스트"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(userService).getUserBoard(any());
    }

    @Test
    @WithAuthUser
    @DisplayName("My QnA List 출력")
    void getMyQnaList() throws Exception {
        Long id = 1L;
        List<QnaDTO> qnaList = new ArrayList<>();

        for (long i=1L; i < 4L; i++) {
            QnaDTO qna = QnaDTO.builder()
                    .id(i)
                    .userId(id)
                    .title("Test Qna Title " + i)
                    .content("Test Qna Content " + i)
                    .completed(true)
                    .answer("Test Qna Answer " + i)
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
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.qnaDTOList[].id").description("QnA ID"),
                                fieldWithPath("_embedded.qnaDTOList[].userId").description("작성자 ID"),
                                fieldWithPath("_embedded.qnaDTOList[].title").description("제목"),
                                fieldWithPath("_embedded.qnaDTOList[].content").description("내용"),
                                fieldWithPath("_embedded.qnaDTOList[].completed").description("완료여부"),
                                fieldWithPath("_embedded.qnaDTOList[].answer").description("작성된 답변 from 관리자"),
                                fieldWithPath("_embedded.qnaDTOList[].regDate").description("생성날짜"),
                                fieldWithPath("_embedded.qnaDTOList[]._links.self.href").description("link to QnA"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getQnaListByUserId(id);
    }
}