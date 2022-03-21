package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.common.SupportType;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SupportFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String originalName;
    @Column(nullable = false, length = 2000)
    private String path;

    private Long size;

    @Enumerated(EnumType.STRING)
    private SupportType supportType;

    private Long supportId;
}
