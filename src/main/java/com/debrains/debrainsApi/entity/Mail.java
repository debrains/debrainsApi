package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.hateoas.TilSerializer;
import com.debrains.debrainsApi.hateoas.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.codehaus.groovy.util.StringUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Mail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long id;

    private String title;

    private String content;

    private String toAddr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
