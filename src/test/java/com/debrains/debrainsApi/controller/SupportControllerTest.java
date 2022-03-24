package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.config.RestDocsConfigurate;
import com.debrains.debrainsApi.config.WithAuthUser;
import com.debrains.debrainsApi.config.SecurityConfig;
import com.debrains.debrainsApi.dto.*;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.hateoas.EventConverter;
import com.debrains.debrainsApi.hateoas.NoticeConverter;
import com.debrains.debrainsApi.hateoas.QnaConverter;
import com.debrains.debrainsApi.security.jwt.JwtAuthenticationFilter;
import com.debrains.debrainsApi.service.SupportService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SupportController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}))
@AutoConfigureRestDocs
@Import(RestDocsConfigurate.class)
class SupportControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SupportService supportService;
    @SpyBean
    private NoticeConverter noticeConverter;
    @SpyBean
    private EventConverter eventConverter;
    @SpyBean
    private QnaConverter qnaConverter;

    @Test
    @DisplayName("공지사항 List 조회")
    @WithAuthUser
    void getNoticeList() throws Exception {
        // given
        List<NoticeDTO> noticeList = new ArrayList<>();

        for (long i=1L; i < 4L; i++) {
            NoticeDTO notice = NoticeDTO.builder()
                    .id(i)
                    .title("notice title " + i)
                    .content("notice content " + i)
                    .open(false)
                    .top(false)
                    .regDate(LocalDateTime.now())
                    .build();
            noticeList.add(notice);
        }
        given(supportService.getNoticeList()).willReturn(noticeList);

        mvc.perform(get("/support/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$..title").exists())
                .andExpect(jsonPath("$..content").exists())
                .andDo(print())
                .andDo(document("get-noticeList",
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
                                fieldWithPath("_embedded.noticeDTOList[].id").description("공지사항 ID"),
                                fieldWithPath("_embedded.noticeDTOList[].title").description("제목"),
                                fieldWithPath("_embedded.noticeDTOList[].content").description("내용"),
                                fieldWithPath("_embedded.noticeDTOList[].viewCnt").description("조회수"),
                                fieldWithPath("_embedded.noticeDTOList[].open").description("공개여부"),
                                fieldWithPath("_embedded.noticeDTOList[].top").description("상단고정여부"),
                                fieldWithPath("_embedded.noticeDTOList[].regDate").description("생성날짜"),
                                fieldWithPath("_embedded.noticeDTOList[]._links.self.href").description("link to Notice"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getNoticeList();
    }

    @Test
    @DisplayName("공지사항 조회")
    @WithAuthUser
    void getNotice() throws Exception {
        // given
        Long id = 1L;
        NoticeDTO dto = NoticeDTO.builder()
                .id(id)
                .title("notice title")
                .content("notice content")
                .open(false)
                .top(false)
                .regDate(LocalDateTime.now())
                .build();
        given(supportService.getNotice(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/support/notice/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print())
                .andDo(document("get-notice",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("list").description("link to List"),
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
                                fieldWithPath("id").description("공지사항 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("viewCnt").description("조회수"),
                                fieldWithPath("open").description("공개여부"),
                                fieldWithPath("top").description("상단고정여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.list.href").description("link to List"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getNotice(any());
    }

    @Test
    @DisplayName("이벤트 List 조회")
    @WithAuthUser
    void getEventList() throws Exception {
        // given
        List<EventDTO> eventList = new ArrayList<>();

        for (long i=1L; i < 4L; i++) {
            EventDTO event = EventDTO.builder()
                    .id(i)
                    .title("event title " + i)
                    .content("event content " + i)
                    .open(false)
                    .ended(false)
                    .regDate(LocalDateTime.now())
                    .build();
            eventList.add(event);
        }
        given(supportService.getEventList()).willReturn(eventList);

        mvc.perform(get("/support/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$..title").exists())
                .andExpect(jsonPath("$..content").exists())
                .andDo(print())
                .andDo(document("get-eventList",
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
                            fieldWithPath("_embedded.eventDTOList[].id").description("이벤트 ID"),
                            fieldWithPath("_embedded.eventDTOList[].title").description("제목"),
                            fieldWithPath("_embedded.eventDTOList[].content").description("내용"),
                            fieldWithPath("_embedded.eventDTOList[].viewCnt").description("조회수"),
                            fieldWithPath("_embedded.eventDTOList[].open").description("공개여부"),
                            fieldWithPath("_embedded.eventDTOList[].ended").description("종료여부"),
                            fieldWithPath("_embedded.eventDTOList[].regDate").description("생성날짜"),
                            fieldWithPath("_embedded.eventDTOList[]._links.self.href").description("link to Event"),
                            fieldWithPath("_links.self.href").description("link to self"),
                            fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getEventList();
    }

    @Test
    @DisplayName("이벤트 조회")
    @WithAuthUser
    void getEvent() throws Exception {
        //given
        Long id = 1L;
        EventDTO dto = EventDTO.builder()
                .id(id)
                .title("event title")
                .content("event content")
                .open(false)
                .ended(false)
                .regDate(LocalDateTime.now())
                .build();
        given(supportService.getEvent(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/support/event/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print())
                .andDo(document("get-event",
                        links(
                            linkWithRel("self").description("link to self"),
                            linkWithRel("list").description("link to List"),
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
                                fieldWithPath("id").description("이벤트 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("viewCnt").description("조회수"),
                                fieldWithPath("open").description("공개여부"),
                                fieldWithPath("ended").description("종료여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.list.href").description("link to List"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getEvent(any());
    }

    @Test
    @DisplayName("작성한 QnA 열람")
    @WithAuthUser
    void getQna() throws Exception {
        // given
        Long id = 1L;
        QnaDTO dto = QnaDTO.builder()
                .id(id)
                .userId(1L)
                .title("Test Qna Title")
                .content("Test Qna Content")
                .completed(true)
                .answer("Test Qna Answer")
                .regDate(LocalDateTime.now())
                .build();
        given(supportService.getQna(any())).willReturn(dto);

        // when & then
        mvc.perform(get("/support/qna/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print())
                .andDo(document("get-qna",
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
                                fieldWithPath("id").description("QnA ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("completed").description("완료여부"),
                                fieldWithPath("answer").description("작성된 답변 from 관리자"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).getQna(any());
    }

    @Test
    @DisplayName("QnA 열람 실패")
    @WithAuthUser
    void getQnaFail() throws Exception {
        // given
        Long id = 1L;
        QnaDTO dto = QnaDTO.builder()
                .id(id)
                .userId(2L)         // 내 QnA가 아닌 글
                .title("Test Qna Title")
                .content("Test Qna Content")
                .completed(true)
                .answer("Test Qna Answer")
                .regDate(LocalDateTime.now())
                .build();
        given(supportService.getQna(any())).willReturn(dto);

        // when&then
        mvc.perform(get("/support/qna/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("QnA 저장")
    @WithAuthUser
    void saveQna() throws Exception {
        // given
        QnaFormDTO formDto = QnaFormDTO.builder()
                .title("QnA Title")
                .content("QnA Content")
                .build();
        QnaDTO dto = QnaDTO.builder()
                .id(1L)
                .userId(1L)
                .title("QnA Title")
                .content("QnA Content")
                .completed(false)
                .regDate(LocalDateTime.now())
                .build();
        given(supportService.saveQna(any())).willReturn(dto);

        // when & then
        mvc.perform(post("/support/qna")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(formDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andDo(print())
                .andDo(document("save-qna",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("create").description("link to create qna"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("userId").description("user id number"),
                                fieldWithPath("title").description("qna title"),
                                fieldWithPath("content").description("qna content")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("QnA ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("completed").description("완료여부"),
                                fieldWithPath("answer").description("작성된 답변 from 관리자"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.create.href").description("link to QnA"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
        verify(supportService).saveQna(any());

    }

    @Test
    @DisplayName("스킬 추가 요청")
    @WithAuthUser
    void requestSkill() throws Exception {
        // given
        SkillReqDTO dto = SkillReqDTO.builder()
                .request("Backend - JAVA 추가 바랍니다")
                .build();

        mvc.perform(post("/support/skill")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("request-skill",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("id").description("skill request ID"),
                                fieldWithPath("request").description("skill request")
                        )
                ));
    }
}