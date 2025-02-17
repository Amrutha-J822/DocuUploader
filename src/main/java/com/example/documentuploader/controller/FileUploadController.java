package com.example.documentuploader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    // Directory where uploaded files will be stored
    private static final String UPLOAD_DIR = "uploads";

    // Display the file upload form
    @GetMapping("/")
    public String index(Model model) {
        return "index";  // Thymeleaf template name (index.html)
    }

    // Handle file upload POST requests
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        // Check if the file is empty
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        try {
            // Normalize the file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Ensure the upload directory exists; if not, create it
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Resolve the file path and write the file bytes
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + fileName + "'.");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "File upload failed: " + e.getMessage());
        }

        return "redirect:/";
    }
}
