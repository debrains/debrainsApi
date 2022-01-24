package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer purpose;
    private String skills;


}
