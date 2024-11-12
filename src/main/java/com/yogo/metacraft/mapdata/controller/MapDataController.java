package com.yogo.metacraft.mapdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yogo.metacraft.common.ApiResponse;
import com.yogo.metacraft.mapdata.document.MapData;
import com.yogo.metacraft.mapdata.document.MapDataDto;
import com.yogo.metacraft.mapdata.repository.MapDataRepository;
import com.yogo.metacraft.mapdata.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final MapDataRepository mapDataRepository;
    private final FirebaseStorageService firebaseService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 문자열을 DTO로 변환하기 위한 ObjectMapper

    @GetMapping("/all")
    public ResponseEntity<List<MapData>> getAllTestData() {
        List<MapData> mapDataList = mapDataRepository.findAll();
        return ResponseEntity.ok(mapDataList);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<MapData> getTestData(@PathVariable Long id) {
//        return mapDataRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    // 썸네일 URL 조회
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(@PathVariable Long id) {
        return mapDataRepository.findById(id)
                .map(mapData -> ResponseEntity.ok(Map.of("url", mapData.getThumbnail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MapData>> updateMapAndImage(
            @RequestParam(value = "thumbnail", required = false) MultipartFile file,
            @RequestParam("testData") String testDataJson) {
        System.out.println("요청 도착");

        try {
            // JSON 문자열을 MapDataDto 객체로 변환
            MapDataDto mapDataDto = objectMapper.readValue(testDataJson, MapDataDto.class);
            System.out.println("Parsed MapDataDto: " + mapDataDto.getInstanceData());

            // Firebase에 이미지 업로드 또는 기본값 설정
            String imageUrl = firebaseService.uploadFile(file);
            System.out.println("Image URL: " + imageUrl);

            // MapData 객체 생성 및 설정
            MapData mapData = new MapData();
            mapData.setMapName(mapDataDto.getMapName());
            mapData.setInstanceData(mapDataDto.getInstanceData());
            mapData.setThumbnail(imageUrl);

            MapData savedMapData = mapDataRepository.save(mapData);

            return ResponseEntity.ok(new ApiResponse<>(true, "File uploaded and data saved successfully", savedMapData));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Failed to parse testData JSON"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to upload file or save data"));
        }
    }

    @PostMapping("/upload/no-image")
    public ResponseEntity<ApiResponse<MapData>> updateMapWithoutImage(@RequestBody MapDataDto mapDataDto) {
        try {
            // 이미지를 사용하지 않으므로 빈 문자열로 설정
            // Firebase에 이미지 업로드 또는 기본값 설정
            String imageUrl = firebaseService.uploadFile(null);
            System.out.println("Image URL: " + imageUrl);

            // MapData 객체 생성 및 설정
            MapData mapData = new MapData();
            mapData.setMapName(mapDataDto.getMapName());
            mapData.setInstanceData(mapDataDto.getInstanceData());
            mapData.setThumbnail(imageUrl);

            // MongoDB에 MapData 저장
            MapData savedMapData = mapDataRepository.save(mapData);

            // 성공 응답과 저장된 데이터 반환
            return ResponseEntity.ok(new ApiResponse<>(true, "Data saved successfully without image", savedMapData));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to save data"));
        }
    }

    @GetMapping("/find")
    public ResponseEntity<?> getMapDataByMapNameQuery(@RequestParam("mapName") String mapName) {
        List<MapData> mapDataList = mapDataRepository.findByMapName(mapName);

        if (mapDataList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "MapData not found for mapName: " + mapName));
        }
        return ResponseEntity.ok(new ApiResponse(true, "MapData retrieved successfully", mapDataList));
    }

    @GetMapping("/{mapName}")
    public ResponseEntity<?> getMapDataByMapNamePath(@PathVariable("mapName") String mapName) {
        List<MapData> mapDataList = mapDataRepository.findByMapName(mapName);

        if (mapDataList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "MapData not found for mapName: " + mapName));
        }
        return ResponseEntity.ok(new ApiResponse(true, "MapData retrieved successfully", mapDataList));
    }
}