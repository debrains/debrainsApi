package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.hateoas.TilSerializer;
import com.debrains.debrainsApi.hateoas.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"til", "user", "files"})
public class TilCrt extends BaseEntity {

    @Id
    @Column(name = "til_crt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "til_id")
    @JsonSerialize(using = TilSerializer.class)
    private Til til;

    private LocalDateTime startTime1;
    private LocalDateTime endTime1;

    private LocalDateTime startTime2;
    private LocalDateTime endTime2;

    private LocalDateTime startTime3;
    private LocalDateTime endTime3;

    private LocalTime watchTime;

    @Column(length = 2000)
    private String description;

    @Builder.Default
    private boolean open = true;

    @Builder.Default
    private boolean denied = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = UserSerializer.class)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "tilCrt", cascade = CascadeType.ALL)
    private List<TilCrtFile> files = new ArrayList<>();

    public void changeTilCrt(TilCrtDTO tilCrtDTO) {
        this.startTime1 = tilCrtDTO.getStartTime1();
        this.endTime1 = tilCrtDTO.getEndTime1();
        this.startTime2 = tilCrtDTO.getStartTime2();
        this.endTime2 = tilCrtDTO.getEndTime2();
        this.startTime3 = tilCrtDTO.getStartTime3();
        this.endTime3 = tilCrtDTO.getEndTime3();
        this.description = tilCrtDTO.getDescription();
    }

    public void updateAdminTilCrt(TilCrtDTO tilCrtDTO){
        this.denied = tilCrtDTO.isDenied();
        this.open = tilCrtDTO.isOpen();
    }

}
