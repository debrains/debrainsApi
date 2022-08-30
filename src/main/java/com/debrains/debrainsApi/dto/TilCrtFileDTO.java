package com.debrains.debrainsApi.dto;

import com.debrains.debrainsApi.entity.TilCrtFile;
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

    public TilCrtFileDTO(TilCrtFile tilcrt) {
        id = tilcrt.getId();
        originalName = tilcrt.getOriginalName();
        fileName = tilcrt.getFileName();
        path = tilcrt.getPath();
    }
}
