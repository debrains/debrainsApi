package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDTO {
    private Long id;
    @NotBlank
    private String title;
    private String content;
    private int viewCnt;
    private boolean open;
    private boolean ended;
    private LocalDateTime regDate;

}
