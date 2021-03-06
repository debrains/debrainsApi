package com.debrains.debrainsApi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TilDTO {

    private Long id;
//    @NotBlank
    private Long userId;
    @NotBlank
    private String subject;
    @NotBlank
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate, endDate;

    @NotBlank
    private String cycleStatus;
    private int cycleCnt;
    private int crtCnt;
    private int totalCnt;

    private boolean expired;

    private LocalDateTime regDate, modDate;
}
