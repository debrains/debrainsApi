package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.hateoas.TilCrtFileSerializer;
import com.debrains.debrainsApi.hateoas.TilSerializer;
import com.debrains.debrainsApi.hateoas.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

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

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalTime watchTime;

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean open;

    @Column(columnDefinition = "boolean default false")
    private boolean denied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = UserSerializer.class)
    private User user;

    @OneToMany(mappedBy = "tilCrt", cascade = CascadeType.ALL)
//    @JsonSerialize(using = TilCrtFileSerializer.class)
    @JsonIgnore
    private List<TilCrtFile> files = new ArrayList<>();

    public void changeTilCrt(TilCrtDTO tilCrtDTO) {
        this.startTime = tilCrtDTO.getStartTime();
        this.endTime = tilCrtDTO.getEndTime();
        this.description = tilCrtDTO.getDescription();
    }

}
