package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.dto.SkillDTO;
import com.debrains.debrainsApi.dto.SkillReqDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.repository.SkillRepository;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/user")
public class AdminUserController {

    private final UserService userService;
    private final SkillRepository skillRepository;

    @GetMapping("")
    public String userList(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
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

    @GetMapping("/skill")
    public String skillList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<SkillDTO> page = userService.getSkillList(pageable);
        model.addAttribute("skills", page);
        return "setting/skill_list";
    }

    @PostMapping("/skill")
    public String saveSkill(SkillDTO dto) {
        userService.saveSkill(dto);
        return "redirect:/root/user/skill";
    }

    @GetMapping("/skillCreate")
    public String saveSkillPage(Model model) {
        List<String> categories = userService.getCategories();
        model.addAttribute("ctgs", categories);
        return "setting/skill_write";
    }

    @GetMapping("/skill/{id}")
    public String deleteSkill(@PathVariable("id") Long id, Model model) {
        userService.deleteSkill(id);
        return "redirect:/root/user/skill";
    }

    @GetMapping("/skillName")
    @ResponseBody
    public boolean validateName(@RequestParam("name") String name) {
        return skillRepository.existsByName(name);
    }

    @GetMapping("/skillreq")
    public String getSkillreqList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<SkillReqDTO> page = userService.getSkillreqList(pageable);
        model.addAttribute("skillreqs", page);
        return "setting/skill_req_list";
    }

    @GetMapping("/skillreq/{id}")
    public String deleteSkillreq(@PathVariable("id") Long id, Model model) {
        userService.deleteSkillreq(id);
        return "redirect:/root/user/skillreq";
    }

}
