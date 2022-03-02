package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.service.admin.AdminSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/support")
public class AdminSupportController {

    private final AdminSupportService adminSupportService;

    @GetMapping("/notice")
    public String noticeListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<NoticeDTO> page = adminSupportService.findNoticeAll(pageable);
        model.addAttribute("noticeList", page);
        return "support/notice_list";
    }

    @PostMapping("/notice")
    public String updateNoticeInfo(NoticeDTO dto) {
        adminSupportService.updateAdminNoticeInfo(dto);
        return "redirect:/root/support/notice";
    }

    @GetMapping("/notice/create")
    public String saveNoticePage() {
        return "support/notice_write";
    }

    @PostMapping("/notice/create")
    public String saveNotice(NoticeDTO dto) {
        adminSupportService.saveNotice(dto);
        return "redirect:/root/support/notice";
    }

    @GetMapping("/notice/{id}")
    public String getNoticeInfo(@PathVariable("id") Long id, Model model) {
        NoticeDTO getNotice = adminSupportService.findNoticeById(id);
        model.addAttribute("notice", getNotice);
        return "support/notice_detail";
    }


    /**
     * Event
     */
    @GetMapping("/event")
    public String eventListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<EventDTO> page = adminSupportService.findEventAll(pageable);
        model.addAttribute("eventList", page);
        return "support/event_list";
    }

    @PostMapping("/event")
    public String updateEventInfo(EventDTO dto) {
        adminSupportService.updateAdminEventInfo(dto);
        return "redirect:/root/support/event";
    }

    @GetMapping("/event/{id}")
    public String getEventInfo(@PathVariable("id") Long id, Model model) {
        EventDTO getEvent = adminSupportService.findEventById(id);
        model.addAttribute("event", getEvent);
        return "support/event_detail";
    }

    @GetMapping("/event/create")
    public String saveEventPage() {
        return "support/event_write";
    }


    @PostMapping("/event/create")
    public String saveEvent(EventDTO dto) {
        adminSupportService.saveEvent(dto);
        return "redirect:/root/support/event";
    }

    /**
     * QnA
     */

    @GetMapping("/qna")
    public String qnaListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<QnaDTO> page = adminSupportService.findQnaAll(pageable);
        model.addAttribute("qnaList", page);
        return "support/qna_list";
    }

    @PostMapping("/qna")
    public String updateQnaInfo(QnaDTO dto) {
        adminSupportService.updateAdminQnaInfo(dto);
        return "redirect:/root/support/qna";
    }

    @GetMapping("/qna/{id}")
    public String getQnaInfo(@PathVariable("id") Long id, Model model) {
        QnaDTO getQna = adminSupportService.findQnaById(id);
        model.addAttribute("qna", getQna);
        return "support/qna_detail";
    }


}