package com.yogo.metacraft.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yogo.metacraft.test.document.TestData;
import com.yogo.metacraft.test.document.TestDataDto;
import com.yogo.metacraft.test.repository.TestDataRepository;
import com.yogo.metacraft.test.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataRepository testDataRepository;
    private final FirebaseStorageService firebaseService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 문자열을 DTO로 변환하기 위한 ObjectMapper

    @GetMapping("/all")
    public ResponseEntity<List<TestData>> getAllTestData() {
        List<TestData> testDataList = testDataRepository.findAll();
        return ResponseEntity.ok(testDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestData> getTestData(@PathVariable Long id) {
        return testDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 썸네일 URL 조회
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(@PathVariable Long id) {
        return testDataRepository.findById(id)
                .map(testData -> ResponseEntity.ok(Map.of("url", testData.getThumbnail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public String updateMapAndImage(@RequestParam("thumbnail") MultipartFile file, @RequestParam("testData") String testDataJson) {
        try {
            // JSON 문자열을 TestDataDto 객체로 변환
            TestDataDto testDataDto = objectMapper.readValue(testDataJson, TestDataDto.class);
            System.out.println(testDataDto.getInstanceData());
            // Firebase에 이미지 업로드
            String imageUrl = firebaseService.uploadFile(file);

            // MongoDB에 저장할 TestData 객체 생성
            TestData testData = new TestData();
            testData.setMapName(testDataDto.getMapName());
            testData.setInstanceData(testDataDto.getInstanceData());
            testData.setThumbnail(imageUrl);

            testDataRepository.save(testData);

            return "File uploaded and data saved successfully: " + imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file or save data";
        }
    }
}