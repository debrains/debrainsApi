package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Builder.Default
    private boolean completed = false;

    @Column(length = 2000)
    private String answer;


    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
