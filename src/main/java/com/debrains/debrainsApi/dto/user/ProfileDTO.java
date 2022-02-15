package com.debrains.debrainsApi.dto.user;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDTO {
    private Long id;
    private Long userId;
    private String purpose;
    private List<String> skills;
}
