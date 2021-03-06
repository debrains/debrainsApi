package com.debrains.debrainsApi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TilCrtDTO {

    private Long id;

    @NotNull
    private Long tilId;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime1, endTime1, startTime2, endTime2, startTime3, endTime3;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime watchTime;

    private boolean open;

    private Long userId;

    private boolean denied;

    private String name;

    private List<TilCrtFileDTO> fileList;

    private LocalDateTime regDate, modDate;
}
