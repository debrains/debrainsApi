package com.debrains.debrainsApi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/setting")
public class AdminSettingController {

    @GetMapping("/interest")
    public String interestListPage() {
        return "setting/interest_list";
    }
}
