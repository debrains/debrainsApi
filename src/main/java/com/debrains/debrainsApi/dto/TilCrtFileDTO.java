package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TilCrtFileDTO {

    private Long id;

    private String originalName;

    private String fileName;

    private String path;

}
