package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.*;
import com.debrains.debrainsApi.entity.*;
import com.debrains.debrainsApi.repository.EventRepository;
import com.debrains.debrainsApi.repository.NoticeRepository;
import com.debrains.debrainsApi.repository.QnaRepository;
import com.debrains.debrainsApi.repository.SkillReqRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class SupportServiceTest {

    @InjectMocks
    private SupportServiceImpl supportService;

    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private QnaRepository qnaRepository;
    @Mock
    private SkillReqRepository skillReqRepository;
    @Mock
    private ModelMapper modelMapper;

    User user;
    Qna qna;
    QnaDTO qnaDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("devdev")
                .email("dev@dev.com")
                .build();
        qna = Qna.builder()
                .id(1L)
                .user(user)
                .title("test")
                .content("test content")
                .answer("test answer")
                .build();
        qnaDto = QnaDTO.builder()
                .id(1L)
                .userId(1L)
                .title("test")
                .content("test content")
                .answer("test answer")
                .build();
    }


    @Test
    @DisplayName("공지사항 조회")
    void getNotice() {
        // given
        Long id = 1L;
        NoticeDTO dto = NoticeDTO.builder()
                .id(id)
                .title("notice title")
                .content("notice content")
                .build();
        Notice entity = Notice.builder()
                .title("notice title")
                .content("notice content")
                .build();
        given(noticeRepository.findById(id)).willReturn(Optional.of(entity));
        given(modelMapper.map(any(Notice.class), any())).willReturn(dto);

        // when
        NoticeDTO notice = supportService.getNotice(id);

        // then
        assertThat(notice.getId()).isEqualTo(id);
        verify(noticeRepository).findById(id);

    }

    @Test
    @DisplayName("공지사항 저장")
    void saveNotice() {
        // given
        NoticeDTO dto = NoticeDTO.builder()
                .id(1L)
                .title("notice title")
                .content("notice content")
                .build();
        Notice entity = Notice.builder()
                .id(1L)
                .title("notice title")
                .content("notice content")
                .build();
        given(noticeRepository.save(any())).willReturn(entity);

        // when
        Long id = supportService.saveNotice(dto);

        // then
        assertThat(id).isEqualTo(dto.getId());
        verify(noticeRepository).save(any());

    }

    @Test
    void deleteNotice() {
        Long id = 1L;
        noticeRepository.deleteById(id);
        verify(noticeRepository).deleteById(id);
    }

    @Test
    void getEvent() {
        // given
        Long id = 1L;
        EventDTO dto = EventDTO.builder()
                .id(id)
                .title("event title")
                .content("event content")
                .build();
        Event entity = Event.builder()
                .title("event title")
                .content("event content")
                .build();
        given(eventRepository.findById(id)).willReturn(Optional.of(entity));
        given(modelMapper.map(any(Event.class), any())).willReturn(dto);

        // when
        EventDTO event = supportService.getEvent(id);

        // then
        assertThat(event.getId()).isEqualTo(id);
        verify(eventRepository).findById(id);
    }

    @Test
    @DisplayName("이벤트 저장")
    void saveEvent() {
        // given
        EventDTO dto = EventDTO.builder()
                .id(1L)
                .title("event title")
                .content("event content")
                .build();
        Event entity = Event.builder()
                .id(1L)
                .title("event title")
                .content("event content")
                .build();
        given(eventRepository.save(any(Event.class))).willReturn(entity);
        given(modelMapper.map(any(EventDTO.class), any())).willReturn(entity);

        // when
        Long eventId = supportService.saveEvent(dto);

        // then
        assertThat(eventId).isEqualTo(dto.getId());
        verify(eventRepository).save(any());
    }

    @Test
    @DisplayName("이벤트 삭제")
    void deleteEvent() {
        Long id = 1L;
        supportService.deleteEvent(id);
        verify(eventRepository).deleteById(id);
    }

    @Test
    @DisplayName("QnA 한건 조회")
    void getQna() {
        // given
        Long id = 1L;
        given(qnaRepository.findById(any())).willReturn(Optional.of(qna));
        given(modelMapper.map(any(Qna.class), any())).willReturn(qnaDto);

        // when
        QnaDTO dto = supportService.getQna(id);

        // then
        assertThat(dto.getId()).isEqualTo(id);
        verify(qnaRepository).findById(any());
    }

    @Test
    @DisplayName("계정별 QnA 리스트")
    void getQnaListByUserId() {
        // given
        Long id = 1L;
        List<Qna> myQnaList = List.of(qna);
        given(qnaRepository.findByUserId(id)).willReturn(myQnaList);
        given(modelMapper.map(any(Qna.class), any())).willReturn(qnaDto);

        // when
        List<QnaDTO> qnaList = supportService.getQnaListByUserId(id);

        // then
        assertThat(qnaList).allSatisfy(qna -> Assertions.assertThat(qna.getUserId()).isEqualTo(id));
        verify(qnaRepository).findByUserId(id);
    }

    @Test
    @DisplayName("QnA 저장")
    void saveQna() {
        // given
        QnaFormDTO form = QnaFormDTO.builder()
                .userId(1L)
                .title("title")
                .content("content")
                .build();

        given(qnaRepository.save(any(Qna.class))).willReturn(qna);
        given(modelMapper.map(any(QnaFormDTO.class), any())).willReturn(qna);
        given(modelMapper.map(any(Qna.class), any())).willReturn(qnaDto);

        // when
        QnaDTO saveQna = supportService.saveQna(form);

        // then
        assertThat(saveQna.getId()).isEqualTo(qnaDto.getId());
        verify(qnaRepository).save(any());

    }

    @Test
    @DisplayName("QnA 답변 작성")
    void updateQnaAnswer() {
        // given
        Long id = 1L;
        given(qnaRepository.findById(any())).willReturn(Optional.of(qna));

        // when
        String answer = "modified answer";
        supportService.updateQnaAnswer(id, answer);

        // then
        Optional<Qna> getQna = qnaRepository.findById(id);
        assertThat(getQna.get().getAnswer()).isEqualTo(answer);
        verify(qnaRepository, times(2)).findById(any());
    }

    @Test
    @DisplayName("스킬추가 요청 리스트")
    void getSkillReqList() {
        // given
        SkillReqDTO dto = SkillReqDTO.builder()
                .request("Backend - JAVA 추가 바랍니다")
                .build();

        SkillReq entity = SkillReq.builder()
                .id(1L)
                .request("Backend - JAVA 추가 바랍니다")
                .build();
        List<SkillReq> list = List.of(entity);

        given(skillReqRepository.findAll()).willReturn(list);
        given(modelMapper.map(any(), any())).willReturn(dto);

        // when
        List<SkillReqDTO> skillReqList = supportService.getSkillReqList();

        // then
        assertThat(skillReqList).size().isEqualTo(1);
        verify(skillReqRepository).findAll();

    }

    @Test
    @DisplayName("스킬추가 요청")
    void saveSkillReq() {
        // given
        SkillReqDTO dto = SkillReqDTO.builder()
                .request("Backend - JAVA 추가 바랍니다")
                .build();

        SkillReq entity = SkillReq.builder()
                .id(1L)
                .request("Backend - JAVA 추가 바랍니다")
                .build();

        given(skillReqRepository.save(any())).willReturn(entity);

        // when
        supportService.saveSkillReq(dto);

        // then
        verify(skillReqRepository).save(any());
    }

    @Test
    @DisplayName("스킬추가 요청 삭제")
    void deleteSkillReq() {
        Long id = 1L;
        supportService.deleteSkillReq(id);
        verify(skillReqRepository).deleteById(any());
    }
}