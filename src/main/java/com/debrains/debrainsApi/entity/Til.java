package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Til extends BaseEntity {

    @Id
    @Column(name = "til_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER 들어가야 함

    @OneToMany(mappedBy = "til")
    private List<TilCrt> tilCrts = new ArrayList<>();

    @Column(nullable = false)
    private String subject;

    @Column(length = 2000)
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private CycleStatus cycleStatus = CycleStatus.EVERYDAY;

    private int cycleCnt;

    @Column(columnDefinition = "int default 0")
    private int crtCnt;

    @Column(columnDefinition = "int default 0")
    private int totalCnt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean expired;

}
