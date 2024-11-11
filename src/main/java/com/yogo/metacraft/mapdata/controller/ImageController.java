package com.yogo.metacraft.mapdata.controller;

import com.yogo.metacraft.mapdata.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor  // final 필드에 대한 생성자 주입
public class ImageController {

    private final FirebaseStorageService firebaseService;  // final 키워드 추가

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            // 파일 타입 검증
            if (!isImageFile(file)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only image files are allowed"));
            }

            String imageUrl = firebaseService.uploadFile(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            response.put("fileName", file.getOriginalFilename());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;
        return contentType.startsWith("image/");
    }
}