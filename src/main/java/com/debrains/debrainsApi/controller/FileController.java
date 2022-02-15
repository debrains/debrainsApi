package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "upload")
    public ResponseEntity<String> upload(MultipartFile file) throws IllegalStateException, IOException {
        fileService.store(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping(value = "download")
    public ResponseEntity<Resource> serveFile(@RequestParam(value = "filename") String filename) {

        Resource file = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value = "delete")
    public ResponseEntity<String> delete(@RequestParam(value = "filename") String filename) {
        fileService.delete(filename);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
