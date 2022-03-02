package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.TilCrtDTO;
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

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalTime watchTime;

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean open;

    @Column(columnDefinition = "boolean default false")
    private boolean denied;

    public void changeTilCrt(TilCrtDTO tilCrtDTO) {
        this.startTime = tilCrtDTO.getStartTime();
        this.endTime = tilCrtDTO.getEndTime();
        this.description = tilCrtDTO.getDescription();
    }

}
