package com.yogo.metacraft.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yogo.metacraft.test.document.InstanceData;
import com.yogo.metacraft.test.document.TestData;
import com.yogo.metacraft.test.document.TestDataDto;
import com.yogo.metacraft.test.repository.TestDataRepository;
import com.yogo.metacraft.test.service.FirebaseStorageService;
import com.yogo.metacraft.test.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    private final MapService mapService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 문자열을 DTO로 변환하기 위한 ObjectMapper

    @PostMapping("/save")
    public ResponseEntity<TestData> saveTestData(@RequestBody TestData testData) {
        return ResponseEntity.ok(testDataRepository.save(testData));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestData>> getAllTestData() {
        System.out.println("요청 받음?");
        List<TestData> testDataList = testDataRepository.findAll();
        return ResponseEntity.ok(testDataList);
    }

//    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<TestData> saveTestData(
//            @RequestParam("mapName") String mapName,
//            @RequestParam("instanceDatas") String instanceDatasJson,
//            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
//        try {
//            // JSON 문자열을 InstanceData 리스트로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<InstanceData> instanceDatas = objectMapper.readValue(
//                    instanceDatasJson,
//                    objectMapper.getTypeFactory().constructCollectionType(List.class, InstanceData.class)
//            );
//
//            // TestData 객체 생성
//            TestData testData = new TestData();
//            testData.setMapName(mapName);
//            testData.setInstanceDatas(instanceDatas);
//
//            return ResponseEntity.ok(mapService.saveMapWithThumbnail(testData, thumbnail));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }

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
    public String uploadImage(@RequestParam("thumbnail") MultipartFile file, @RequestParam("testData") String testDataJson) {
        try {
            // JSON 문자열을 TestDataDto 객체로 변환
            TestDataDto testDataDto = objectMapper.readValue(testDataJson, TestDataDto.class);

            // Firebase에 이미지 업로드
            String imageUrl = firebaseService.uploadFile(file);

            // MongoDB에 저장할 TestData 객체 생성
            TestData testData = new TestData();
            testData.setMapName(testDataDto.getMapName());
            testData.setInstanceDatas(testDataDto.getInstanceDatas());
            testData.setThumbnail(imageUrl);

            testDataRepository.save(testData);

            return "File uploaded and data saved successfully: " + imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file or save data";
        }
    }
}