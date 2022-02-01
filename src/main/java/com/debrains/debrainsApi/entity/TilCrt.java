package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(exclude = "til")
public class TilCrt extends BaseEntity {

    @Id
    @Column(name = "til_crt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "til_id")
    private Til til;

    private String file;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Temporal(TemporalType.TIME)
    private Date watchTime;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean open;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean denied;

}
