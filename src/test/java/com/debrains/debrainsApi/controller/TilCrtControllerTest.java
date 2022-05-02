package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.config.RestDocsConfigurate;
import com.debrains.debrainsApi.config.WithAuthUser;
import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.debrains.debrainsApi.service.TilCrtService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Import(RestDocsConfigurate.class)
public class TilCrtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TilService tilService;

    @Autowired
    TilCrtService tilCrtService;

    @Test
    @DisplayName("TIL인증_생성")
    @WithAuthUser
    void createTilCrt() throws Exception {

        // Given
        TilDTO til = createTil();

        MockMultipartFile files = getFiles();
        TilCrtDTO tilCrtDTO = TilCrtDTO.builder()
                .tilId(til.getId())
                .description("tilCrt")
                .startTime1(LocalDateTime.now().minusHours(4))
                .endTime1(LocalDateTime.now())
                .watchTime(LocalTime.of(5, 0, 0))
                .build();

        MockMultipartFile metadata = new MockMultipartFile("tilCrtDTO", "tilCrtDTO",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(tilCrtDTO).getBytes());

        // When, Then
        mockMvc.perform(multipart("/til-crts/")
//                .file(files)
                .file(metadata)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andDo(document("create-tilcrt",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("til-crts").description("link to query til-crts"),
                                linkWithRel("update").description("link to update"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestParts(
                                partWithName("tilCrtDTO").description("TIL 인증")
//                                partWithName("files").description("사진")
                        ),
                        relaxedRequestPartFields(
                                "tilCrtDTO",
                                fieldWithPath("tilId").description("TIL ID"),
                                fieldWithPath("description").description("인증 상세내용"),
                                fieldWithPath("startTime1").description("시작일1"),
                                fieldWithPath("endTime1").description("종료일1"),
                                fieldWithPath("startTime2").description("시작일2"),
                                fieldWithPath("endTime2").description("종료일2"),
                                fieldWithPath("startTime3").description("시작일3"),
                                fieldWithPath("endTime3").description("종료일3"),
                                fieldWithPath("watchTime").description("스탑워치")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL_CRT ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("tilId").description("TIL ID"),
                                fieldWithPath("description").description("인증 상세내용"),
                                fieldWithPath("startTime1").description("시작일1"),
                                fieldWithPath("endTime1").description("종료일1"),
                                fieldWithPath("startTime2").description("시작일2"),
                                fieldWithPath("endTime2").description("종료일2"),
                                fieldWithPath("startTime3").description("시작일3"),
                                fieldWithPath("endTime3").description("종료일3"),
                                fieldWithPath("watchTime").description("스탑워치"),
                                fieldWithPath("open").description("공개여부(0: 비공개, 1: 공개)"),
                                fieldWithPath("denied").description("관리자 승인(0: 승인, 1: 반려)"),
                                fieldWithPath("fileList").description("인증사진"),
                                fieldWithPath("regDate").description("생성날짜"),
                                fieldWithPath("modDate").description("수정날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.til-crts.href").description("link to til-crts"),
                                fieldWithPath("_links.update.href").description("link to update"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @Test
    @DisplayName("til_인증_조회")
    @WithAuthUser
    public void queryTilCrts() throws Exception {
        TilDTO til = createTil();
        IntStream.range(0, 5).forEach(i -> {
            try {
                createTilCrt(til.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(get("/til-crts/")
                .param("tilId", til.getId().toString())
                .param("page", "0") // page는 0부터 시작
                .param("size", "10")
                .param("sort", "id,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.tilCrtDTOList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-tilcrtList",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.tilCrtDTOList[].id").description("TIL_CRT ID"),
                                fieldWithPath("_embedded.tilCrtDTOList[].userId").description("작성자 ID"),
                                fieldWithPath("_embedded.tilCrtDTOList[].tilId").description("TIL ID"),
                                fieldWithPath("_embedded.tilCrtDTOList[].description").description("인증 상세내용"),
                                fieldWithPath("_embedded.tilCrtDTOList[].startTime1").description("시작일1").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].endTime1").description("종료일1").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].startTime2").description("시작일2").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].endTime2").description("종료일2").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].startTime3").description("시작일3").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].endTime3").description("종료일3").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].watchTime").description("스탑워치").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[].fileList[]").description("인증파일").optional(),
                                fieldWithPath("_embedded.tilCrtDTOList[]._links.self.href").description("link to self"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile"),
                                fieldWithPath("page.size").description("한 페이지 당 게시물 개수"),
                                fieldWithPath("page.totalElements").description("총 게시물 수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("현재 페이지 번호")
                        )
                ));
    }

    @Test
    @DisplayName("til_인증_상세보기")
    @WithAuthUser
    public void getTilCrts() throws Exception {
        TilDTO til = createTil();
        TilCrtDTO tilCrt = createTilCrt(til.getId());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/til-crts/{id}", tilCrt.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-tilcrt",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("update").description("link to update"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL_CRT ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL_CRT ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("tilId").description("TIL ID"),
                                fieldWithPath("description").description("인증 상세내용"),
                                fieldWithPath("startTime1").description("시작일1"),
                                fieldWithPath("endTime1").description("종료일1"),
                                fieldWithPath("startTime2").description("시작일2"),
                                fieldWithPath("endTime2").description("종료일2"),
                                fieldWithPath("startTime3").description("시작일3"),
                                fieldWithPath("endTime3").description("종료일3"),
                                fieldWithPath("watchTime").description("스탑워치"),
                                fieldWithPath("fileList[]").description("인증파일").optional(),
                                fieldWithPath("open").description("공개여부"),
                                fieldWithPath("denied").description("관리자승인여부"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile"),
                                fieldWithPath("_links.update.href").description("link to update")
                        )
                ));
    }

    @Test
    @DisplayName("til_인증_수정")
    @WithAuthUser
    public void updateTilCrts() throws Exception {

        // Given
        TilDTO til = createTil();
        TilCrtDTO tilCrtDTO = createTilCrt(til.getId());
        MockMultipartFile files = getFiles();

        String description = "til Crt modify";
        tilCrtDTO.setDescription(description);

        MockMultipartFile metadata = new MockMultipartFile("tilCrtDTO", "tilCrtDTO",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(tilCrtDTO).getBytes());

        // When, Then
        mockMvc.perform(
                RestDocumentationRequestBuilders.fileUpload("/til-crts/{id}", tilCrtDTO.getId())
//                        .file(files)
                        .file(metadata)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-tilcrt",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("id").description("TIL_CRT ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestParts(
                                partWithName("tilCrtDTO").description("TIL 인증")
//                                partWithName("files").description("사진")
                        ),
                        relaxedRequestPartFields(
                                "tilCrtDTO",
                                fieldWithPath("tilId").description("TIL ID"),
                                fieldWithPath("description").description("인증 상세내용"),
                                fieldWithPath("startTime1").description("시작일1"),
                                fieldWithPath("endTime1").description("종료일1"),
                                fieldWithPath("startTime2").description("시작일2"),
                                fieldWithPath("endTime2").description("종료일2"),
                                fieldWithPath("startTime3").description("시작일3"),
                                fieldWithPath("endTime3").description("종료일3")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("TIL_CRT ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("tilId").description("TIL ID"),
                                fieldWithPath("description").description("인증 상세내용"),
                                fieldWithPath("startTime1").description("시작일1"),
                                fieldWithPath("endTime1").description("종료일1"),
                                fieldWithPath("startTime2").description("시작일2"),
                                fieldWithPath("endTime2").description("종료일2"),
                                fieldWithPath("startTime3").description("시작일3"),
                                fieldWithPath("endTime3").description("종료일3"),
                                fieldWithPath("watchTime").description("스탑워치"),
                                fieldWithPath("fileList[]").description("인증파일").optional(),
                                fieldWithPath("open").description("공개여부"),
                                fieldWithPath("denied").description("관리자승인여부"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @Test
    @DisplayName("til_인증_삭제")
    @WithAuthUser
    public void deleteTilCrts() throws Exception {
        TilDTO til = createTil();
        TilCrtDTO tilCrt = createTilCrt(til.getId());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/til-crts/{id}", tilCrt.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-tilcrt",
                        pathParameters(
                                parameterWithName("id").description("TIL_CRT ID")
                        )));
    }

    public MockMultipartFile getFiles() {
        return new MockMultipartFile("files", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
    }

    private TilDTO createTil() {
        TilDTO tilDTO = TilDTO.builder()
                .userId(1L)
                .subject("TIL subject 입니다.")
                .description("TIL description 입니다")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusWeeks(8))
                .cycleStatus(CycleStatus.WEEK.toString())
                .cycleCnt(4)
                .build();

        return tilService.createTil(tilDTO);
    }

    private TilCrtDTO createTilCrt(Long tilId) throws IOException {

        MultipartFile[] files = new MockMultipartFile[]{
                new MockMultipartFile("files", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes())
        };

        TilCrtDTO tilCrtDTO = TilCrtDTO.builder()
                .userId(1L)
                .tilId(tilId)
                .description("인증 설명입니다.")
                .startTime1(LocalDateTime.now().minusHours(4))
                .endTime1(LocalDateTime.now())
                .watchTime(LocalTime.of(5, 0, 0))
                .build();

//        return tilCrtService.createTilCrts(files, tilCrtDTO);
        return tilCrtService.createTilCrts(null, tilCrtDTO);
    }
}
