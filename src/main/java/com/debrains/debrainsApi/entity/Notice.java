package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.common.UserRole;
import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Notice extends BaseEntity{
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
    private boolean top = false;
    private Integer viewCnt;

    public void updateAdminNoticeInfo(NoticeDTO dto){
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.open = dto.isOpen();
        this.top = dto.isTop();
    }
}
