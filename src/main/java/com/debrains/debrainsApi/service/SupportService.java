package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.SupportType;
import com.debrains.debrainsApi.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SupportService {

    NoticeDTO getNotice(Long id);
    List<NoticeDTO> getNoticeList();
    Long saveNotice(MultipartFile[] files, NoticeDTO notice) throws IOException;
    void deleteNotice(Long id);
    Page<NoticeDTO> getAdminNoticeList(Pageable pageable);
    void updateAdminNoticeInfo(NoticeDTO dto, MultipartFile[] files) throws IOException;

    EventDTO getEvent(Long id);
    List<EventDTO> getEventList();
    Long saveEvent(EventDTO event, MultipartFile[] files) throws IOException;
    void deleteEvent(Long id);
    Page<EventDTO> getAdminEventList(Pageable pageable);
    void updateAdminEventInfo(EventDTO dto, MultipartFile[] files) throws IOException;

    QnaDTO getQna(Long id);
    List<QnaDTO> getQnaList();
    List<QnaDTO> getQnaListByUserId(Long userId);
    QnaDTO saveQna(QnaFormDTO qna);
    void updateQnaAnswer(Long id, String answer);
    Page<QnaDTO> getAdminQnaList(Pageable pageable);

    List<SkillReqDTO> getSkillReqList();
    void saveSkillReq(SkillReqDTO skill);
    void deleteSkillReq(Long id);

    List<SupportFileDTO> getAdminSupportFiles(Long id, SupportType type);

    SupportFileDTO getSupportFileById(Long id);

    void deleteFile(Long id);
}
