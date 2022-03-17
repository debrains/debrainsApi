package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkillDTO {

    private Long id;
    private String category;
    private int seq;
    private String name;

}
