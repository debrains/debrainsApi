package com.debrains.debrainsApi.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class Til extends BaseEntity {

    @Id
    @Column(name = "til_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER 들어가야 함

    /*@OneToMany(mappedBy = "til")
    private List<TilCrt> tilCrts = new ArrayList<>();*/

    @Column(nullable = false)
    private String subject;

    @Column(length = 2000)
    private String description;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CycleStatus cycleStatus = CycleStatus.EVERYDAY;

    private int cycleCnt;

    @Column(columnDefinition = "int default 0")
    private int crtCnt;

    @Column(columnDefinition = "int default 0")
    private int totalCnt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean expired;

    /**
     * 총 인증 횟수
     * */
    public void totalCrtCount() {

        Period period = (Period.between(startDate, endDate));
        int diffDate = period.getDays() + 1;

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
     * TODO:: 목록 출력할 때 확인하여 업데이트(낱개, 리스트)
     * */
    public void expiredCheck(){
        LocalDate now = LocalDate.now();
        if(this.endDate.isBefore(now)){
            this.expired = true;
        }
    }

    /**
     * 인증 횟수 추가/감소
     * */
    public void addCrtCnt(){
        this.crtCnt += 1;
    }

    public void removeCrtCnt(){
        this.crtCnt -= 1;
    }
}
