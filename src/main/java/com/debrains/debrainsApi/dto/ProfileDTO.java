package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileDTO {

    private Long id;
    private Long userId;
    private int purpose;
    private String skills;

}
