package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.common.SupportType;
import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.dto.QnaDTO;
import com.debrains.debrainsApi.dto.SupportFileDTO;
import com.debrains.debrainsApi.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/support")
public class AdminSupportController {

    private final SupportService supportService;

    @GetMapping("/notice")
    public String noticeListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<NoticeDTO> page = supportService.getAdminNoticeList(pageable);
        model.addAttribute("noticeList", page);
        return "support/notice_list";
    }

    @PostMapping("/notice")
    public String updateNoticeInfo(NoticeDTO dto, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {
        supportService.updateAdminNoticeInfo(dto, files);
        return "redirect:/root/support/notice";
    }

    @GetMapping("/notice/create")
    public String saveNoticePage() {
        return "support/notice_write";
    }

    @PostMapping("/notice/create")
    public String saveNotice(NoticeDTO dto, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {
        supportService.saveNotice(files, dto);
        return "redirect:/root/support/notice";
    }

    @GetMapping("/notice/{id}")
    public String getNoticeInfo(@PathVariable("id") Long id, Model model) {
        NoticeDTO getNotice = supportService.getNotice(id);
        List<SupportFileDTO> files = supportService.getAdminSupportFiles(id, SupportType.Notice);
        model.addAttribute("notice", getNotice);
        model.addAttribute("files", files);
        return "support/notice_detail";
    }


    /**
     * Event
     */
    @GetMapping("/event")
    public String eventListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<EventDTO> page = supportService.getAdminEventList(pageable);
        model.addAttribute("eventList", page);
        return "support/event_list";
    }

    @PostMapping("/event")
    public String updateEventInfo(EventDTO dto, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {
        supportService.updateAdminEventInfo(dto, files);
        return "redirect:/root/support/event";
    }

    @GetMapping("/event/{id}")
    public String getEventInfo(@PathVariable("id") Long id, Model model) {
        EventDTO getEvent = supportService.getEvent(id);
        List<SupportFileDTO> files = supportService.getAdminSupportFiles(id, SupportType.Event);
        model.addAttribute("files", files);
        model.addAttribute("event", getEvent);
        return "support/event_detail";
    }

    @GetMapping("/event/create")
    public String saveEventPage() {
        return "support/event_write";
    }


    @PostMapping("/event/create")
    public String saveEvent(EventDTO dto, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {
        supportService.saveEvent(dto, files);
        return "redirect:/root/support/event";
    }

    /**
     * QnA
     */

    @GetMapping("/qna")
    public String qnaListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<QnaDTO> page = supportService.getAdminQnaList(pageable);
        model.addAttribute("qnaList", page);
        return "support/qna_list";
    }

    @PostMapping("/qna")
    public String updateQnaInfo(QnaDTO dto) {
        supportService.updateQnaAnswer(dto.getId(), dto.getAnswer());
        return "redirect:/root/support/qna";
    }

    @GetMapping("/qna/{id}")
    public String getQnaInfo(@PathVariable("id") Long id, Model model) {
        QnaDTO getQna = supportService.getQna(id);
        model.addAttribute("qna", getQna);
        return "support/qna_detail";
    }

    /**
     * 파일 download
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("id") Long id) throws MalformedURLException {
        SupportFileDTO file = supportService.getSupportFileById(id);
        UrlResource resource = new UrlResource(file.getPath());
        String contentDispostion = "attachment; filename=\"" + file.getOriginalName() + "\"";
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispostion).body(resource);
    }

    @GetMapping("/delete")
    @ResponseBody
    public String fileDelete(@RequestParam Long id) {
        supportService.deleteFile(id);
        return "succ";
    }

}