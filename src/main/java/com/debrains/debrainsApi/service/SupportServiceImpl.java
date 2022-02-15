package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.dto.QnaFormDTO;
import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import com.debrains.debrainsApi.entity.Qna;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.EventRepository;
import com.debrains.debrainsApi.repository.NoticeRepository;
import com.debrains.debrainsApi.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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
    public void updateQnaAnswer(Long id, String answer) {
        Qna entity = qnaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_CONTENT));
        entity.updateAnswer(answer);
    }
}
