package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.FileDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class TilCrt extends BaseEntity {

    @Id
    @Column(name = "til_crt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "til_id")
    private Til til;

    private String filePath;
    private String fileName;
    private String oriFileName;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @Column(columnDefinition = "TIME")
    private LocalTime watchTime;

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean open;

    @Column(columnDefinition = "boolean default false")
    private boolean denied;

    public void createFile(FileDTO fileDTO) {
        this.filePath = fileDTO.getFilePath();
        this.fileName = fileDTO.getFileName();
        this.oriFileName = fileDTO.getOriFileName();
    }

}
