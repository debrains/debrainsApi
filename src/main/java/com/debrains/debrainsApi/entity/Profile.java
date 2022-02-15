package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.user.ProfileDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private String purpose;
    private String skills;


    public void updateProfile(ProfileDTO dto) {
        this.purpose = dto.getPurpose();
        if (dto.getSkills() != null) {
            this.skills = String.join(",", dto.getSkills());
        } else {
            this.skills = "";
        }
    }

}
