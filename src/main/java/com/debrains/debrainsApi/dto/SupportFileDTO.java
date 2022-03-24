package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupportFileDTO {


    private Long id;

    private String originalName;

    private String fileName;

    private String path;

    private String supportType;

    private Long supportId;
}
