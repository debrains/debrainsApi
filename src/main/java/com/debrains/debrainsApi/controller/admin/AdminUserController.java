package com.debrains.debrainsApi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/user")
public class AdminUserController {

    @GetMapping("")
    public String userListPage(Model model) {
        return "user/user_list";
    }

    @GetMapping("/{userIdx}")
    public String userDetailPage(@PathVariable("userIdx") int user_id) {
        return "user/user_detail";
    }

}
