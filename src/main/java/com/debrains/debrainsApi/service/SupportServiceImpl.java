package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.*;
import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import com.debrains.debrainsApi.entity.Qna;
import com.debrains.debrainsApi.entity.SkillReq;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.EventRepository;
import com.debrains.debrainsApi.repository.NoticeRepository;
import com.debrains.debrainsApi.repository.QnaRepository;
import com.debrains.debrainsApi.repository.SkillReqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SupportServiceImpl implements SupportService {

    private final NoticeRepository noticeRepository;
    private final EventRepository eventRepository;
    private final QnaRepository qnaRepository;
    private final SkillReqRepository skillReqRepository;
    private final ModelMapper modelMapper;

    @Override
    public NoticeDTO getNotice(Long id) {
        Notice entity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_CONTENT));
        NoticeDTO dto = modelMapper.map(entity, NoticeDTO.class);
        return dto;
    }

    @Override
    public List<NoticeDTO> getNoticeList() {
        List<NoticeDTO> noticeList = noticeRepository.findAll()
                .stream().map(entity -> modelMapper.map(entity, NoticeDTO.class))
                .collect(Collectors.toList());
        return noticeList;
    }

    @Override
    public Long saveNotice(NoticeDTO dto) {
        Notice entity = modelMapper.map(dto, Notice.class);
        return noticeRepository.save(entity).getId();
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public Page<NoticeDTO> getAdminNoticeList(Pageable pageable) {
        Page<NoticeDTO> noticeList = noticeRepository.findAll(pageable)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class));
        return noticeList;
    }

    @Override
    @Transactional
    public void updateAdminNoticeInfo(NoticeDTO dto) {
        Notice notice = noticeRepository.getById(dto.getId());
        notice.updateAdminNoticeInfo(dto);
    }

    @Override
    public EventDTO getEvent(Long id) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_CONTENT));
        EventDTO dto = modelMapper.map(entity, EventDTO.class);
        return dto;
    }

    @Override
    public List<EventDTO> getEventList() {
        List<EventDTO> eventList = eventRepository.findAll()
                .stream().map(entity -> modelMapper.map(entity, EventDTO.class))
                .collect(Collectors.toList());
        return eventList;
    }

    @Override
    public Long saveEvent(EventDTO dto) {
        Event entity = modelMapper.map(dto, Event.class);
        return eventRepository.save(entity).getId();
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Page<EventDTO> getAdminEventList(Pageable pageable) {
        Page<EventDTO> eventList = eventRepository.findAll(pageable)
                .map(event -> modelMapper.map(event, EventDTO.class));
        return eventList;
    }

    @Override
    @Transactional
    public void updateAdminEventInfo(EventDTO dto) {
        Event event = eventRepository.getById(dto.getId());
        event.updateAdminEventInfo(dto);
    }

    @Override
    public QnaDTO getQna(Long id) {
        Qna entity = qnaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_CONTENT));
        QnaDTO dto = modelMapper.map(entity, QnaDTO.class);
        return dto;
    }

    @Override
    public List<QnaDTO> getQnaList() {
        List<QnaDTO> qnaList = qnaRepository.findAll()
                .stream().map(entity -> modelMapper.map(entity, QnaDTO.class))
                .collect(Collectors.toList());
        return qnaList;
    }

    @Override
    public List<QnaDTO> getQnaListByUserId(Long userId) {
        List<QnaDTO> qnaList = qnaRepository.findByUserId(userId)
                .stream().map(qna -> modelMapper.map(qna, QnaDTO.class)).collect(Collectors.toList());
        return qnaList;
    }

    @Override
    public QnaDTO saveQna(QnaFormDTO formDto) {
        Qna formEntity = modelMapper.map(formDto, Qna.class);
        Qna entity = qnaRepository.save(formEntity);
        return modelMapper.map(entity, QnaDTO.class);
    }

    @Override
    @Transactional
    public void updateQnaAnswer(Long id, String answer) {
        Qna entity = qnaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_CONTENT));
        entity.updateAnswer(answer);
    }

    @Override
    public Page<QnaDTO> getAdminQnaList(Pageable pageable) {
        Page<QnaDTO> qnaList = qnaRepository.findAll(pageable)
                .map(qna -> modelMapper.map(qna, QnaDTO.class));
        return qnaList;
    }

    @Override
    public List<SkillReqDTO> getSkillReqList() {
        List<SkillReqDTO> list = skillReqRepository.findAll()
                .stream().map(entity -> modelMapper.map(entity, SkillReqDTO.class)).collect(Collectors.toList());
        return list;
    }

    @Override
    public void saveSkillReq(SkillReqDTO skill) {
        SkillReq entity = modelMapper.map(skill, SkillReq.class);
        skillReqRepository.save(entity);
    }

    @Override
    public void deleteSkillReq(Long id) {
        skillReqRepository.deleteById(id);
    }
}
