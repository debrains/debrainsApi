package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.RestDocsConfigurate;
import com.debrains.debrainsApi.dto.TilCrtDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    TilRepository tilRepository;

    @Test
    @DisplayName("TIL인증_생성")
    public void createTilCrt() throws Exception {

        // Given
        Til til = createTil();

        TilCrtDTO tilCrtDTO = TilCrtDTO.builder()
                .tilId(til.getId())
                .description("인증 설명입니다.")
                .file("ddd")
                .startTime(LocalDateTime.of(2022, 2, 10, 10, 48, 11))
                .endTime(LocalDateTime.of(2022, 2, 10, 15, 48, 11))
                .watchTime(LocalTime.of(5, 0, 0))
                .open(true)
                .build();

        // When, Then
        mockMvc.perform(post("/til-crts/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(tilCrtDTO)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("til_인증_생성_빈값")
    public void createTilCrtEmpty() throws Exception {
        TilCrtDTO tilCrtDTO = TilCrtDTO.builder().build();

        mockMvc.perform(post("/til-crts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tilCrtDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("til_인증_생성_잘못된값")
    public void createTilCrtWrongValue() throws Exception {

    }

    @Test
    @DisplayName("til_인증_조회")
    public void queryTilCrts() throws Exception {

    }

    @Test
    @DisplayName("til_인증_상세보기")
    public void getTilCrts() throws Exception {

    }


    @Test
    @DisplayName("til_인증_조회_잘못된값")
    public void getTilCrtsWrong() throws Exception {

    }

    @Test
    @DisplayName("til_인증_수정")
    public void updateTilCrts() throws Exception {

    }

    @Test
    @DisplayName("til_인증_삭제")
    public void deleteTilCrts() throws Exception {

    }

    /*public MockMultipartFile getFiles() {

        return new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
    }*/

    private Til createTil() {

        Til til = Til.builder()
                .subject("TIL subject1 입니다.")
                .description("TIL description1 입니다")
                .startDate(LocalDate.of(2022, 1, 30))
                .endDate(LocalDate.of(2022, 2, 3))
                .cycleStatus(CycleStatus.WEEK)
                .cycleCnt(4)
                .build();
        til.totalCrtCount();
        return tilRepository.save(til);
    }
}
