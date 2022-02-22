package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.dto.NoticeDTO;
import com.debrains.debrainsApi.service.admin.AdminSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public String noticeListPage(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
        Page<NoticeDTO> page = adminSupportService.findAll(pageable);
        model.addAttribute("noticeList", page);
        return "support/notice_list";
    }

    @PostMapping("/notice")
    public String updateNoticeInfo(NoticeDTO dto) {
        adminSupportService.updateAdminNoticeInfo(dto);
        return "redirect:/root/support/notice/" + dto.getId();
    }

    @GetMapping("/notice/{id}")
    public String getNoticeInfo(@PathVariable("id") Long id, Model model) {
        NoticeDTO getNotice = adminSupportService.findById(id);
        model.addAttribute("notice", getNotice);
        return "support/notice_detail";
    }


    @GetMapping("/qna")
    public String qnaListPage() {
        return "support/qna_list";
    }

    @GetMapping("/qna/{qnaIdx}")
    public String qnaDetailPage(@PathVariable("qnaIdx") int qna_id) {
        return "support/qna_detail";
    }

}
