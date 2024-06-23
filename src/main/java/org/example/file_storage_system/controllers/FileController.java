package org.example.file_storage_system.controllers;

import org.example.file_storage_system.Models.File;
import org.example.file_storage_system.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("username") String username,
                             Model model) {
        try {
            fileService.store(file, username);
            model.addAttribute("message", "File uploaded successfully");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "upload";
    }

    @GetMapping("/files")
    public String listFiles(Model model) {
        List<File> files = fileService.getAllFiles();
        model.addAttribute("files", files);
        return "files";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        byte[] data = fileService.downloadFile(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        File file = fileService.getFile(id).orElseThrow();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @PostMapping("/send")
    public String sendFile(@RequestParam Long fileId,
                           @RequestParam String recipientUsername,
                           Model model) {
        try {
            fileService.sendFile(fileId, recipientUsername);
            model.addAttribute("message", "File sent successfully");
        } catch (Exception e) {
            model.addAttribute("message", "Failed to send file: " + e.getMessage());
        }
        return "redirect:/files";
    }
}