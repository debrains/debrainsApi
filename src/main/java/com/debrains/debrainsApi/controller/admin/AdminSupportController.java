package com.debrains.debrainsApi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/support")
public class AdminSupportController {

    @GetMapping("/notice")
    public String noticeListPage() {
        return "support/notice_list";
    }

    @GetMapping("/notice/{noticeIdx}")
    public String noticeDetailPage(@PathVariable("noticeIdx") int notice_id) {
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
