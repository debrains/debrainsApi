package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.config.RestDocsConfigurate;
import com.debrains.debrainsApi.config.WithAuthUser;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.entity.Til;
import com.debrains.debrainsApi.service.TilService;
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
    TilService tilService;

    @Test
    @WithAuthUser
    @DisplayName("TIL??????_??????")
    public void createTil() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject1 ?????????.")
                .description("TIL description1 ?????????")
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
                                linkWithRel("update").description("link to update"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("userId").description("????????? ID"),
                                fieldWithPath("subject").description("??????"),
                                fieldWithPath("description").description("?????? ????????????"),
                                fieldWithPath("startDate").description("?????????"),
                                fieldWithPath("endDate").description("?????????"),
                                fieldWithPath("cycleStatus").description("??????(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("userId").description("????????? ID"),
                                fieldWithPath("subject").description("??????"),
                                fieldWithPath("description").description("?????? ????????????"),
                                fieldWithPath("startDate").description("?????????"),
                                fieldWithPath("endDate").description("?????????"),
                                fieldWithPath("cycleStatus").description("??????(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("?????? ??????"),
                                fieldWithPath("crtCnt").description("?????? ?????? ??????"),
                                fieldWithPath("totalCnt").description("?????????????????? ??? ?????? ??????"),
                                fieldWithPath("expired").description("????????????"),
                                fieldWithPath("regDate").description("????????????"),
                                fieldWithPath("modDate").description("????????????")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("TIL??????_??????_??????")
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
    @DisplayName("TIL??????_?????????_???_??????")
    public void createTil_Bad_Request_Wrong_Input() throws Exception {
        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject1 ?????????.")
                .description("TIL description1 ?????????")
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
    @DisplayName("til????????? ????????????")
    public void queryTils() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(i -> {
            generateTil(i);
        });

        //When & Then
        mockMvc.perform(get("/tils")
                .param("page", "0") // page??? 0?????? ??????
                .param("size", "10")
                .param("sort", "id,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.tilDTOList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-tilList",
                        links(
                                linkWithRel("self").description("?????? ?????????"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.tilDTOList[].id").description("TIL ID"),
                                fieldWithPath("_embedded.tilDTOList[].userId").description("????????? ID"),
                                fieldWithPath("_embedded.tilDTOList[].subject").description("??????"),
                                fieldWithPath("_embedded.tilDTOList[].description").description("?????? ????????????"),
                                fieldWithPath("_embedded.tilDTOList[].startDate").description("?????????"),
                                fieldWithPath("_embedded.tilDTOList[].endDate").description("?????????"),
                                fieldWithPath("_embedded.tilDTOList[].cycleStatus").description("??????(EVERYDAY, WEEK)"),
                                fieldWithPath("_embedded.tilDTOList[].cycleCnt").description("?????? ??????"),
                                fieldWithPath("_embedded.tilDTOList[].crtCnt").description("?????? ?????? ??????"),
                                fieldWithPath("_embedded.tilDTOList[].totalCnt").description("?????????????????? ??? ?????? ??????"),
                                fieldWithPath("_embedded.tilDTOList[].expired").description("????????????"),
                                fieldWithPath("_embedded.tilDTOList[].regDate").description("????????????"),
                                fieldWithPath("_embedded.tilDTOList[].modDate").description("????????????"),
                                fieldWithPath("_embedded.tilDTOList[]._links.self.href").description("link to self"),
                                fieldWithPath("_links.self.href").description("?????? ?????????"),
                                fieldWithPath("_links.profile.href").description("link to profile"),
                                fieldWithPath("page.size").description("??? ????????? ??? ????????? ??????"),
                                fieldWithPath("page.totalElements").description("??? ????????? ???"),
                                fieldWithPath("page.totalPages").description("??? ????????? ???"),
                                fieldWithPath("page.number").description("?????? ????????? ??????")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("????????? til??? ?????? ????????????")
    public void getTil() throws Exception {
        TilDTO til = generateTil(1);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/tils/{id}", til.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("subject").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-til",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("update").description("link to update"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("userId").description("????????? ID"),
                                fieldWithPath("subject").description("??????"),
                                fieldWithPath("description").description("?????? ????????????"),
                                fieldWithPath("startDate").description("?????????"),
                                fieldWithPath("endDate").description("?????????"),
                                fieldWithPath("cycleStatus").description("??????(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("?????? ??????"),
                                fieldWithPath("crtCnt").description("?????? ?????? ??????"),
                                fieldWithPath("totalCnt").description("?????????????????? ??? ?????? ??????"),
                                fieldWithPath("expired").description("????????????"),
                                fieldWithPath("regDate").description("????????????"),
                                fieldWithPath("modDate").description("????????????"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile"),
                                fieldWithPath("_links.update.href").description("link to update")
                        )));
    }

    @Test
    @WithAuthUser
    @DisplayName("?????? til??? ???????????? ??? 404 ????????????")
    public void getEvent404() throws Exception {
        mockMvc.perform(get("/tils/123456"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAuthUser
    @DisplayName("til ??????")
    public void updateTil() throws Exception {
        // Given
        TilDTO tilDTO = generateTil(1);
        String tilSubject = "Updated Til";
        tilDTO.setSubject(tilSubject);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/tils/{id}", tilDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("subject").exists())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-til",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("subject").description("??????"),
                                fieldWithPath("description").description("?????? ????????????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("TIL ID"),
                                fieldWithPath("userId").description("????????? ID"),
                                fieldWithPath("subject").description("??????"),
                                fieldWithPath("description").description("?????? ????????????"),
                                fieldWithPath("startDate").description("?????????"),
                                fieldWithPath("endDate").description("?????????"),
                                fieldWithPath("cycleStatus").description("??????(EVERYDAY, WEEK)"),
                                fieldWithPath("cycleCnt").description("?????? ??????"),
                                fieldWithPath("crtCnt").description("?????? ?????? ??????"),
                                fieldWithPath("totalCnt").description("?????????????????? ??? ?????? ??????"),
                                fieldWithPath("expired").description("????????????"),
                                fieldWithPath("regDate").description("????????????"),
                                fieldWithPath("modDate").description("????????????"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )));
    }

    @Test
    @WithAuthUser
    @DisplayName("til ??????")
    void deleteTil() throws Exception {
        TilDTO til = generateTil(1);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/tils/{id}", til.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-til",
                        pathParameters(
                                parameterWithName("id").description("TIL ID")
                        )));
    }

    @Test
    @WithAuthUser
    @DisplayName("til ?????? ?????? ??????")
    void currentTil() throws Exception {
        mockMvc.perform(get("/tils/current")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("current-til"));
    }

    private TilDTO generateTil(int index) {

        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject" + index + " ?????????.")
                .description("TIL description" + index + " ?????????")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusWeeks(8))
                .cycleStatus(CycleStatus.WEEK.toString())
                .cycleCnt(4)
                .build();

        return tilService.createTil(tilDTO);
    }
}