package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.RestDocsConfigurate;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfigurate.class)
class TilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("TIL생성_성공")
    public void createTil() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .memberId(1L)
                .subject("TIL subject1 입니다.")
                .description("TIL description1 입니다")
                .startDate(LocalDate.of(2022, 1, 30))
                .endDate(LocalDate.of(2022, 2, 3))
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
                                linkWithRel("query-tils").description("link to query tils"),
                                linkWithRel("update-til").description("link to update til"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("memberId").description("member id of new til"),
                                fieldWithPath("subject").description("subject of new til"),
                                fieldWithPath("description").description("description of new til"),
                                fieldWithPath("startDate").description("start date of new til"),
                                fieldWithPath("endDate").description("end date of new til"),
                                fieldWithPath("cycleStatus").description("cycle status of new til"),
                                fieldWithPath("cycleCnt").description("cycle cnt of new til")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                /*fieldWithPath("tilId").description("til id"),
                                fieldWithPath("memberId").description("member id of new til"),*/
                                fieldWithPath("id").description("id"),
                                fieldWithPath("subject").description("제목"),
                                fieldWithPath("description").description("상세내용"),
                                fieldWithPath("startDate").description("시작날짜"),
                                fieldWithPath("endDate").description("종료날짜"),
                                fieldWithPath("cycleStatus").description("EVERYDAY, WEEK"),
                                fieldWithPath("cycleCnt").description("주 몇회"),
                                fieldWithPath("crtCnt").description("인증 횟수"),
                                fieldWithPath("totalCnt").description("완료해야하는 총 인증 횟수"),
                                fieldWithPath("expired").description("만료여부"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("modDate").description("수정날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-tils.href").description("link to query tils"),
                                fieldWithPath("_links.update-til.href").description("link to update til"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @Test
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
    @DisplayName("TIL생성_잘못된_값_에러")
    public void createTil_Bad_Request_Wrong_Input() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .memberId(1L)
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
}