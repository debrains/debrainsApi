package com.debrains.debrainsApi.controller.admin;

import com.debrains.debrainsApi.dto.TilCrtDTO;
import com.debrains.debrainsApi.dto.TilCrtFileDTO;
import com.debrains.debrainsApi.dto.TilDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.service.TilCrtService;
import com.debrains.debrainsApi.service.TilService;
import com.debrains.debrainsApi.service.UserService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/root/til")
public class AdminTilController {

    private final TilService tilService;
    private final TilCrtService tilCrtService;
    private final UserService userService;

    /**
     * admin til
     */
    @GetMapping("")
    public String tilListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<TilDTO> page = tilService.getAdminTilList(pageable);
        model.addAttribute("tilList", page);
        return "til/til_list";
    }

    @GetMapping("/{id}")
    public String tilDetailPage(@PathVariable("id") Long id, Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        TilDTO getTil = tilService.getTil(id);
        Page<TilCrtDTO> getTilCrt = tilCrtService.getTilCrtById(getTil.getId(), pageable);
        model.addAttribute("til", getTil);
        model.addAttribute("tilcrtList", getTilCrt);
        return "til/til_detail";
    }


    /**
     * admin til 인증
     */
    @GetMapping("/crt")
    public String tilCrtListPage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<TilCrtDTO> page = tilCrtService.getAdminTilcrtList(pageable);
        model.addAttribute("tilcrtList", page);
        return "til/til_crt_list";
    }

    @PostMapping("/crt")
    public String updateTilcrt(TilCrtDTO tilcrt) {
        tilCrtService.updateAdminTilCrt(tilcrt);
        return "redirect:/root/til/crt/" + tilcrt.getId();
    }

    @GetMapping("/crt/{id}")
    public String tilCrtDetailPage(@PathVariable("id") Long id, Model model) {
        TilCrtDTO getTilcrt = tilCrtService.getTilcrt(id);
        UserInfoDTO user = userService.getUserInfo(getTilcrt.getUserId());
        getTilcrt.setName(user.getName());

        List<TilCrtFileDTO> getFiles = tilCrtService.getTilcrtFiles(id);

        model.addAttribute("files", getFiles);
        model.addAttribute("crt", getTilcrt);
        return "til/til_crt_detail";
    }

    /**
     * 파일 download
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("id") Long id) throws MalformedURLException {
        TilCrtFileDTO file = tilCrtService.getTilCrtFileById(id);
        String originName = file.getOriginalName();
        String fileName = file.getFileName();

        UrlResource resource = new UrlResource("file:" + file.getPath() + File.separator + originName);

        String encodedUploadFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        String contentDispostion = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispostion).body(resource);

    }


}
