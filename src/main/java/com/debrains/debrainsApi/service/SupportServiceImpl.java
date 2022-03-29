package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.AwsS3Uploader;
import com.debrains.debrainsApi.common.SupportType;
import com.debrains.debrainsApi.dto.*;
import com.debrains.debrainsApi.entity.*;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final SupportFileRepository fileRepository;

    private final ModelMapper modelMapper;
    private final AwsS3Uploader awsS3Uploader;

    private static String dirName = "support";

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
    public Long saveNotice(MultipartFile[] files, NoticeDTO dto) throws IOException {
        Notice entity = modelMapper.map(dto, Notice.class);
        Notice notice = noticeRepository.save(entity);
        saveFile(files, notice.getId(), SupportType.Notice);

        return notice.getId();
    }

    @Override
    @Transactional
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
    public void updateAdminNoticeInfo(NoticeDTO dto, MultipartFile[] files) throws IOException {
        Notice notice = noticeRepository.getById(dto.getId());
        notice.updateAdminNoticeInfo(dto);
        saveFile(files, notice.getId(), SupportType.Notice);
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
    public Long saveEvent(EventDTO dto, MultipartFile[] files) throws IOException {
        Event entity = modelMapper.map(dto, Event.class);
        Event event = eventRepository.save(entity);
        saveFile(files, event.getId(), SupportType.Event);
        return event.getId();
    }

    @Override
    @Transactional
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
    public void updateAdminEventInfo(EventDTO dto, MultipartFile[] files) throws IOException {
        Event event = eventRepository.getById(dto.getId());
        event.updateAdminEventInfo(dto);
        saveFile(files, event.getId(), SupportType.Event);
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

    @Override
    public List<SupportFileDTO> getAdminSupportFiles(Long id, SupportType type) {
        List<SupportFileDTO> files = fileRepository.findSupportById(id, type)
                .stream().map(file -> modelMapper.map(file, SupportFileDTO.class)).collect(Collectors.toList());
        return files;
    }

    @Override
    public SupportFileDTO getSupportFileById(Long id) {
        SupportFile file = fileRepository.findById(id).orElseThrow();
        SupportFileDTO dto = modelMapper.map(file, SupportFileDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    public void saveFile(MultipartFile[] files, Long id, SupportType type) throws IOException {
        if (files != null && !files[0].isEmpty()) {
            for (MultipartFile file:files) {
                String path = awsS3Uploader.upload(file, dirName);

                SupportFile supportFile = SupportFile.builder()
                        .fileName(path.substring(path.indexOf(dirName)))
                        .originalName(file.getOriginalFilename())
                        .path(path)
                        .size(file.getSize())
                        .supportId(id)
                        .supportType(type)
                        .build();
                fileRepository.save(supportFile);
            }
        }
    }
}
