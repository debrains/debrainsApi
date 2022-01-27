package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminAuthController {

    @GetMapping("/root/login")
    public String adminLogin(){
        return "user/login";
    }

    @GetMapping("/auth/redirect")
    public String adminAuth() {
        return "user/auth";
    }

}
