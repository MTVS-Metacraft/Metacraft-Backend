package com.yogo.metacraft.mapdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yogo.metacraft.mapdata.document.MapData;
import com.yogo.metacraft.mapdata.document.MapDataDto;
import com.yogo.metacraft.mapdata.repository.MapDataRepository;
import com.yogo.metacraft.mapdata.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapDataController {

    private final MapDataRepository testDataRepository;
    private final FirebaseStorageService firebaseService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 문자열을 DTO로 변환하기 위한 ObjectMapper

    @GetMapping("/all")
    public ResponseEntity<List<MapData>> getAllTestData() {
        List<MapData> mapDataList = testDataRepository.findAll();
        return ResponseEntity.ok(mapDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapData> getTestData(@PathVariable Long id) {
        return testDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 썸네일 URL 조회
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(@PathVariable Long id) {
        return testDataRepository.findById(id)
                .map(mapData -> ResponseEntity.ok(Map.of("url", mapData.getThumbnail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public String updateMapAndImage(@RequestParam("thumbnail") MultipartFile file, @RequestParam("testData") String testDataJson) {
        try {
            // JSON 문자열을 TestDataDto 객체로 변환
            MapDataDto mapDataDto = objectMapper.readValue(testDataJson, MapDataDto.class);
            System.out.println(mapDataDto.getInstanceData());
            // Firebase에 이미지 업로드
            String imageUrl = firebaseService.uploadFile(file);

            // MongoDB에 저장할 TestData 객체 생성
            MapData mapData = new MapData();
            mapData.setMapName(mapDataDto.getMapName());
            mapData.setInstanceData(mapDataDto.getInstanceData());
            mapData.setThumbnail(imageUrl);

            testDataRepository.save(mapData);

            return "File uploaded and data saved successfully: " + imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file or save data";
        }
    }
}