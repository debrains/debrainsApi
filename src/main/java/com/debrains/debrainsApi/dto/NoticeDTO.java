package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeDTO {
    private Long id;
    @NotBlank
    private String title;
    private String content;
    private int viewCnt;
    private Boolean open;
    private Boolean top;

    private LocalDateTime regDate;
}
