package com.debrains.debrainsApi.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MailDTO {

    private String toAddr;
    private String title;
    private String content;
    @NotNull
    private Long userId;

}
