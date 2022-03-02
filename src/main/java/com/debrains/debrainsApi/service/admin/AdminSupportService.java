package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSupportService {
    Page<NoticeDTO> findNoticeAll(Pageable pageable);

    NoticeDTO findNoticeById(Long id);

    void updateAdminNoticeInfo(NoticeDTO dto);

    default NoticeDTO toDtoNotice(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCnt(notice.getViewCnt())
                .build();
    }

    Long saveNotice(NoticeDTO dto);

    Page<EventDTO> findEventAll(Pageable pageable);

    void updateAdminEventInfo(EventDTO dto);

    EventDTO findEventById(Long id);

    Long saveEvent(EventDTO dto);

    Page<QnaDTO> findQnaAll(Pageable pageable);

    QnaDTO findQnaById(Long id);

    void updateAdminQnaInfo(QnaDTO dto);
}
