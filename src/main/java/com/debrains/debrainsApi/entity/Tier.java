package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startExp;
    private Long endExp;
}
