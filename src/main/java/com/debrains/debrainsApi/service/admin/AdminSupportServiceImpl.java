package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import com.debrains.debrainsApi.repository.admin.AdminEventRepository;
import com.debrains.debrainsApi.repository.admin.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class AdminSupportServiceImpl implements AdminSupportService {

    private final AdminNoticeRepository adminNoticeRepository;
    private final AdminEventRepository adminEventRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<NoticeDTO> findNoticeAll(Pageable pageable) {
        Page<NoticeDTO> noticeList = adminNoticeRepository.findAll(pageable)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class));
        return noticeList;
    }

    @Override
    public NoticeDTO findNoticeById(Long id) {
        Notice noticeInfo = adminNoticeRepository.findById(id)
                .orElseThrow();

        NoticeDTO notice = modelMapper.map(noticeInfo, NoticeDTO.class);
        return notice;
    }

    @Override
    public void updateAdminNoticeInfo(NoticeDTO dto) {
        Notice notice = adminNoticeRepository.getById(dto.getId());
        notice.updateAdminNoticeInfo(dto);
    }

    @Override
    public Long saveNotice(NoticeDTO dto) {
        Notice entity = modelMapper.map(dto, Notice.class);
        return adminNoticeRepository.save(entity).getId();
    }

    @Override
    public Page<EventDTO> findEventAll(Pageable pageable) {
        Page<EventDTO> eventList = adminEventRepository.findAll(pageable)
                .map(event -> modelMapper.map(event, EventDTO.class));
        return eventList;
    }

    @Override
    public void updateAdminEventInfo(EventDTO dto) {
        Event event = adminEventRepository.getById(dto.getId());
        event.updateAdminEventInfo(dto);
    }

    @Override
    public EventDTO findEventById(Long id) {
        Event event = adminEventRepository.findById(id)
                .orElseThrow();
        EventDTO result = modelMapper.map(event, EventDTO.class);
        return result;
    }

    @Override
    public Long saveEvent(EventDTO dto) {
        Event entity = modelMapper.map(dto, Event.class);
        return adminEventRepository.save(entity).getId();
    }
}
