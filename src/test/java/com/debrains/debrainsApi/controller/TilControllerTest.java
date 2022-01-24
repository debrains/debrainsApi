package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.entity.CycleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
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

        mockMvc.perform(post("/api/tils/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    @DisplayName("TIL생성_빈값_에러")
    public void createTil_Bad_Request_Empty_Input() throws Exception {
        TilDTO tilDTO = TilDTO.builder().build();

        mockMvc.perform(post("/api/tils/")
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

        mockMvc.perform(post("/api/tils/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tilDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}