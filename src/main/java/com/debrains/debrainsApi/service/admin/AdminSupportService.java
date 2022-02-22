package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.entity.Notice;
import com.debrains.debrainsApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSupportService {
    Page<NoticeDTO> findAll(Pageable pageable);

    NoticeDTO findById(Long id);

    void updateAdminNoticeInfo(NoticeDTO dto);

//    void save(NoticeDTO notice);

    default NoticeDTO toDtoNotice(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCnt(notice.getViewCnt())
                .build();
    }


    Long saveNotice(NoticeDTO dto);
}
