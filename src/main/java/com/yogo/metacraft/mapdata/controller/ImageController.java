package com.yogo.metacraft.mapdata.controller;

import com.yogo.metacraft.mapdata.service.FirebaseStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Image", description = "이미지 업로드 및 관리 API")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final FirebaseStorageService firebaseService;

    @Operation(summary = "이미지 업로드", description = "Cloud Storage에 이미지를 업로드하고 이미지 URL을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 이미지 파일이 아님",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류 - 이미지 업로드 실패",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping()
    public ResponseEntity<Map<String, String>> uploadImage(
            @Parameter(description = "업로드할 이미지 파일", required = true,
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")))
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

    @Operation(summary = "이미지 삭제", description = "Cloud Storage에서 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "파일이 존재하지 않음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류 - 이미지 삭제 실패",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping()
    public ResponseEntity<Map<String, String>> deleteImage(
            @Parameter(description = "삭제할 이미지 파일 이름 (URL에서 추출된 파일명)", required = true)
            @RequestParam("fileName") String fileName) {
        try {
            firebaseService.deleteFile(fileName);
            return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to delete image: " + e.getMessage()));
        }
    }

    /**
     * 이미지 파일인지 확인하는 메서드
     *
     * @param file 업로드된 파일
     * @return 파일이 이미지인지 여부
     */
    @Operation(summary = "이미지 파일 검증", description = "파일이 이미지 형식인지 검증합니다.")
    @Parameter(name = "file", description = "검증할 파일", required = true, schema = @Schema(type = "string", format = "binary"))
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;
        return contentType.startsWith("image/");
    }
}