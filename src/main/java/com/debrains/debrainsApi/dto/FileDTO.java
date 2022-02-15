package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FileDTO {

    private String filePath;
    private String fileName;
    private String oriFileName;

}
