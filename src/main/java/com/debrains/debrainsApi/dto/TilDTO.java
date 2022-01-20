package com.debrains.debrainsApi.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TilDTO {

//    @NotEmpty
    private Long memberId;
    @NotEmpty
    private String subject;
    @NotEmpty
    private String description;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private String cycleStatus;
    private int cycleCnt;
}
