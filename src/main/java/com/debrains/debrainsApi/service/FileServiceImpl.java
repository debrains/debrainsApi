package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.FileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    public FileDTO store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR : File is empty.");
            }
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }

            try (InputStream inputStream = file.getInputStream()) {
                String oriFileName = file.getOriginalFilename();
                String filename = UUID.randomUUID().toString();
                String ext = oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
                filename = filename + "." + ext;

                Files.copy(inputStream, root.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                FileDTO fileDTO = FileDTO.builder()
                        .fileName(filename)
                        .filePath(uploadPath)
                        .oriFileName(file.getOriginalFilename())
                        .build();

                return fileDTO;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path load(String filename) {
        return Paths.get(uploadPath).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        // 파일 하나씩 지우는거 구현
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }
}
