package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer category;
    private Integer seq;

}
