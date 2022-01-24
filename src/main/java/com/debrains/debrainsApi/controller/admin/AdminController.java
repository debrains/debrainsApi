package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/root")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/login")
    public String adminLogin(){
        return "user/login";
    }


    @GetMapping("/user")
    public String userListPage(Model model) {
        return "user/user_list";
    }

    @GetMapping("/user/{userIdx}")
    public String userDetailPage(@PathVariable("userIdx") int user_id) {
        return "user/user_detail";
    }


    @GetMapping("/til")
    public String tilListPage() {
        return "til/til_list";
    }

    @GetMapping("/til/{tilIdx}")
    public String tilDetailPage(@PathVariable("tilIdx") int til_id) {
        return "til/til_detail";
    }


    @GetMapping("/til-crt")
    public String tilCrtListPage() {
        return "til/til_crt_list";
    }

    @GetMapping("/til-crt/{crtIdx}")
    public String tilCrtDetailPage(@PathVariable("crtIdx") int til_id) {
        return "til/til_crt_detail";
    }


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

    @GetMapping("/interest")
    public String interestListPage() {
        return "setting/interest_list";
    }


}
