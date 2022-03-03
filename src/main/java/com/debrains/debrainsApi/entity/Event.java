package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.EventDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Event extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String content;

    @Builder.Default
    private boolean open = true;
    @Builder.Default
    private boolean ended = false;
    private Integer viewCnt;

    public void updateAdminEventInfo(EventDTO eventDTO){
        this.title = eventDTO.getTitle();
        this.content = eventDTO.getContent();
        this.open = eventDTO.getOpen();
        this.ended = eventDTO.getEnded();
    }
}
