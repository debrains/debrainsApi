package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.entity.Notice;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.admin.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class AdminSupportServiceImpl implements AdminSupportService {

    private final AdminNoticeRepository adminNoticeRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<NoticeDTO> findAll(Pageable pageable) {
        Page<NoticeDTO> noticeList = adminNoticeRepository.findAll(pageable)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class));
        return noticeList;
    }

    @Override
    public NoticeDTO findById(Long id) {
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

  /*  @Override
    public void save(NoticeDTO noticeDto) {
        adminNoticeRepository.save(toNoticeDto(noticeDto));
    }*/
}
