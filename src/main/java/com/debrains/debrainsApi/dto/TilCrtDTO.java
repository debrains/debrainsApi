package com.debrains.debrainsApi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TilCrtDTO {

    @NotNull
    private Long tilId;

    @NotBlank
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime, endTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime watchTime;

    private boolean open;
}
