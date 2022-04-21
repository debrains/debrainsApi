package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.TilDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"user", "tilCrts"})
public class Til extends BaseEntity {

    @Id
    @Column(name = "til_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "til", cascade = CascadeType.ALL)
    private List<TilCrt> tilCrts = new ArrayList<>();

    @Column(nullable = false)
    private String subject;

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CycleStatus cycleStatus = CycleStatus.EVERYDAY;

    private int cycleCnt;

    @Column(columnDefinition = "int default 0")
    private int crtCnt;

    @Column(columnDefinition = "int default 0")
    private int totalCnt;

    @Builder.Default
    private boolean expired = false;

    public void changeTil(TilDTO tilDTO) {
        this.subject = tilDTO.getSubject();
        this.description = tilDTO.getDescription();
    }

    /**
     * 총 인증 횟수
     */
    public void totalCrtCount() {

        long diff = ChronoUnit.DAYS.between(startDate, endDate);
        int diffDate = (int) (diff) + 1;

        if (this.cycleStatus.equals(CycleStatus.EVERYDAY)) {
            this.totalCnt = diffDate;
        } else {
            int tempCycleCnt = (diffDate / 7) * this.cycleCnt;
            if ((diffDate % 7) > this.cycleCnt) {
                this.totalCnt = tempCycleCnt + this.cycleCnt;
            } else {
                this.totalCnt = tempCycleCnt + (diffDate % 7);
            }
        }
    }

    /**
     * TIL 유효한지 확인
     */
    public void expiredCheck() {
        LocalDate now = LocalDate.now();
        if (this.endDate.isBefore(now)) {
            this.expired = true;
        }
    }

    /**
     * 인증 횟수 추가/감소
     */
    public void addCrtCnt() {
        this.crtCnt += 1;
        this.user.calExp(100L);
    }

    public void removeCrtCnt() {
        this.crtCnt -= 1;
        this.user.calExp(-100L);
    }
}
