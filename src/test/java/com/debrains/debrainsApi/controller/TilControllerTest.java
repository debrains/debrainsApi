package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.config.RestDocsConfigurate;
import com.debrains.debrainsApi.config.WithAuthUser;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.repository.TilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfigurate.class)
@Transactional
class TilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TilRepository tilRepository;

    @Test
    @WithAuthUser
    @DisplayName("TIL생성_성공")
    public void createTil() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject1 입니다.")
                .description("TIL description1 입니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusWeeks(8))
                .cycleStatus(CycleStatus.WEEK.toString())
                .cycleCnt(4)
                .build();

        mockMvc.perform(post("/tils/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(document("create-til",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("tils").description("link to query tils"),
                                linkWithRel("update").description("link to update til"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("userId").description("작성자"),
                                fieldWithPath("subject").description("목표"),
                                fieldWithPath("description").description("목표 상세내용"),
                                fieldWithPath("startDate").description("시작일"),
                                fieldWithPath("endDate").description("종료일"),
                                fieldWithPath("cycleStatus").description("종류(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("인증 횟수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("subject").description("목표"),
                                fieldWithPath("description").description("목표 상세내용"),
                                fieldWithPath("startDate").description("시작일"),
                                fieldWithPath("endDate").description("종료일"),
                                fieldWithPath("cycleStatus").description("종류(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("인증 횟수"),
                                fieldWithPath("crtCnt").description("현재 인증 횟수"),
                                fieldWithPath("totalCnt").description("완료해야하는 총 인증 횟수"),
                                fieldWithPath("expired").description("만료여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("modDate").description("수정날짜")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("TIL생성_빈값_에러")
    public void createTil_Bad_Request_Empty_Input() throws Exception {
        TilDTO tilDTO = TilDTO.builder().build();

        mockMvc.perform(post("/tils/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithAuthUser
    @DisplayName("TIL생성_잘못된_값_에러")
    public void createTil_Bad_Request_Wrong_Input() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject1 입니다.")
                .description("TIL description1 입니다")
                .startDate(LocalDate.of(2022, 1, 30))
                .endDate(LocalDate.of(2022, 2, 20))
                .cycleStatus(CycleStatus.WEEK.toString())
                .cycleCnt(0)
                .build();

        mockMvc.perform(post("/tils/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithAuthUser
    @DisplayName("til리스트 조회하기")
    public void queryTils() throws Exception {
        //Given
        IntStream.range(0, 5).forEach(i -> {
            generateTil(i);
        });

        //When & Then
        mockMvc.perform(get("/tils")
                .param("page", "1") // page는 0부터 시작
                .param("size", "10")
                .param("sort", "id,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.tilList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-tils",
                        links(
                                linkWithRel("first").description("link to first"),
                                linkWithRel("prev").description("link to prev"),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("next").description("link to next"),
                                linkWithRel("last").description("link to last"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.tilList[].id").description("TIL ID"),
                                fieldWithPath("_embedded.tilList[].subject").description("목표"),
                                fieldWithPath("_embedded.tilList[].description").description("목표 상세내용"),
                                fieldWithPath("_embedded.tilList[].startDate").description("시작일"),
                                fieldWithPath("_embedded.tilList[].endDate").description("종료일"),
                                fieldWithPath("_embedded.tilList[].cycleStatus").description("종류(EVERYDAY, WEEK)"),
                                fieldWithPath("_embedded.tilList[].cycleCnt").description("인증 횟수"),
                                fieldWithPath("_embedded.tilList[].crtCnt").description("현재 인증 횟수"),
                                fieldWithPath("_embedded.tilList[].totalCnt").description("완료해야하는 총 인증 횟수"),
                                fieldWithPath("_embedded.tilList[].expired").description("만료여부"),
                                fieldWithPath("_embedded.tilList[].regDate").description("생성날짜"),
                                fieldWithPath("_embedded.tilList[].modDate").description("수정날짜"),
                                fieldWithPath("_embedded.tilList[]._links.self.href").description("현재 api 주소"),
                                fieldWithPath("_links.first.href").description("처음 페이지"),
                                fieldWithPath("_links.prev.href").description("이전 페이지"),
                                fieldWithPath("_links.self.href").description("현재 페이지"),
                                fieldWithPath("_links.next.href").description("다음 페이지"),
                                fieldWithPath("_links.last.href").description("마지막 페이지"),
                                fieldWithPath("_links.profile.href").description("해당 api의 문서"),
                                fieldWithPath("page.size").description("한 페이지 당 게시물 개수"),
                                fieldWithPath("page.totalElements").description("총 게시물 수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("현재 페이지 번호")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("기존의 til을 하나 조회하기")
    public void getTil() throws Exception {
        Til til = Til.builder()
                .id(33L)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/tils/{id}", til.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("subject").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-til",
                        links(
                                linkWithRel("self").description("현재 api 주소"),
                                linkWithRel("update").description("link to update til"),
                                linkWithRel("profile").description("해당 api의 문서")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("subject").description("목표"),
                                fieldWithPath("description").description("목표 상세내용"),
                                fieldWithPath("startDate").description("시작일"),
                                fieldWithPath("endDate").description("종료일"),
                                fieldWithPath("cycleStatus").description("종류(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("인증 횟수"),
                                fieldWithPath("crtCnt").description("현재 인증 횟수"),
                                fieldWithPath("totalCnt").description("완료해야하는 총 인증 횟수"),
                                fieldWithPath("expired").description("만료여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("modDate").description("수정날짜")
                        )));
    }

    @Test
    @WithAuthUser
    @DisplayName("없는 til을 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        mockMvc.perform(get("/tils/123456"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAuthUser
    @DisplayName("til 수정")
    public void updateTil() throws Exception {
        // Given
        Til til = generateTil(1);
        TilDTO tilDTO = modelMapper.map(til, TilDTO.class);
        String tilSubject = "Updated Til";
        tilDTO.setSubject(tilSubject);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/tils/{id}", til.getId())
                .contentType(MediaType.APPLICATION_JSON) // 내가 보내는 데이터의 타입을 알려줌
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("subject").exists())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-til",
                        links(
                                linkWithRel("self").description("현재 api 주소"),
                                linkWithRel("profile").description("해당 api의 문서")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("subject").description("목표"),
                                fieldWithPath("description").description("목표 상세내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("subject").description("목표"),
                                fieldWithPath("description").description("목표 상세내용"),
                                fieldWithPath("startDate").description("시작일"),
                                fieldWithPath("endDate").description("종료일"),
                                fieldWithPath("cycleStatus").description("종류(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("인증 횟수"),
                                fieldWithPath("crtCnt").description("현재 인증 횟수"),
                                fieldWithPath("totalCnt").description("완료해야하는 총 인증 횟수"),
                                fieldWithPath("expired").description("만료여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("modDate").description("수정날짜")
                        )));
    }

    @Test
    @WithAuthUser
    @DisplayName("til 삭제")
    void deleteTil() throws Exception {
        Til til = Til.builder()
                .id(34L)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/tils/{id}", til.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-til",
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        )));
    }

    private Til generateTil(int index) {

        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject 입니다.")
                .description("TIL description 입니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusWeeks(8))
                .cycleStatus(CycleStatus.WEEK.toString())
                .cycleCnt(4)
                .build();
        Til til = modelMapper.map(tilDTO, Til.class);
        til.totalCrtCount();
        return tilRepository.save(til);
    }
}