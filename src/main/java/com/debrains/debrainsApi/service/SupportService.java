package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupportService {

    NoticeDTO getNotice(Long id);
    List<NoticeDTO> getNoticeList();
    Long saveNotice(NoticeDTO notice);
    void deleteNotice(Long id);
    Page<NoticeDTO> getAdminNoticeList(Pageable pageable);
    void updateAdminNoticeInfo(NoticeDTO dto);

    EventDTO getEvent(Long id);
    List<EventDTO> getEventList();
    Long saveEvent(EventDTO event);
    void deleteEvent(Long id);
    Page<EventDTO> getAdminEventList(Pageable pageable);
    void updateAdminEventInfo(EventDTO dto);

    QnaDTO getQna(Long id);
    List<QnaDTO> getQnaList();
    List<QnaDTO> getQnaListByUserId(Long userId);
    QnaDTO saveQna(QnaFormDTO qna);
    void updateQnaAnswer(Long id, String answer);
    Page<QnaDTO> getAdminQnaList(Pageable pageable);

    List<SkillReqDTO> getSkillReqList();
    void saveSkillReq(SkillReqDTO skill);
    void deleteSkillReq(Long id);



}
