package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.service.UserService;
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
@RequestMapping("/root/user")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("")
    public String userListPage(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
        Page<UserDTO> page = userService.getUserList(pageable);
        model.addAttribute("userList", page);
        return "user/user_list";
    }

    @PostMapping("")
    public String saveUserInfo(UserDTO dto) {
        userService.updateAdminUserInfo(dto);
        return "redirect:/root/user/" + dto.getId();
    }

    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable("id") Long id, Model model) {
        UserDTO getUser = userService.getAdminUserInfo(id);
        model.addAttribute("user", getUser);
        return "user/user_detail";
    }

}
