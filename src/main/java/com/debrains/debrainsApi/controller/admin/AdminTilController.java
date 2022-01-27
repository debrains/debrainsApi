package com.debrains.debrainsApi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/til")
public class AdminTilController {

    @GetMapping("")
    public String tilListPage() {
        return "til/til_list";
    }

    @GetMapping("/{tilIdx}")
    public String tilDetailPage(@PathVariable("tilIdx") int til_id) {
        return "til/til_detail";
    }


    @GetMapping("/crt")
    public String tilCrtListPage() {
        return "til/til_crt_list";
    }

    @GetMapping("/crt/{crtIdx}")
    public String tilCrtDetailPage(@PathVariable("crtIdx") int til_id) {
        return "til/til_crt_detail";
    }
}
