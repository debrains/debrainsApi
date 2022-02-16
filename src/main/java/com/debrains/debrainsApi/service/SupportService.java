package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.*;

import java.util.List;

public interface SupportService {

    NoticeDTO getNotice(Long id);
    List<NoticeDTO> getNoticeList();
    Long saveNotice(NoticeDTO notice);
    void deleteNotice(Long id);

    EventDTO getEvent(Long id);
    List<EventDTO> getEventList();
    Long saveEvent(EventDTO event);
    void deleteEvent(Long id);

    QnaDTO getQna(Long id);
    List<QnaDTO> getQnaList();
    List<QnaDTO> getQnaListByUserId(Long userId);
    QnaDTO saveQna(QnaFormDTO qna);
    void updateQnaAnswer(Long id, String answer);

    List<SkillReqDTO> getSkillReqList();
    void saveSkillReq(SkillReqDTO skill);
    void deleteSkillReq(Long id);
}
