package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TilCurDTO {
    private Long totalCnt;
    private Long ingCnt;
    private Long succCnt;
    private Long failCnt;
}
