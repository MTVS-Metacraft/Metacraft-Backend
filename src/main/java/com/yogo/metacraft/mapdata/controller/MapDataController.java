package com.yogo.metacraft.mapdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yogo.metacraft.common.CustomApiResponse;
import com.yogo.metacraft.common.SimpleApiResponse;
import com.yogo.metacraft.mapdata.document.*;
import com.yogo.metacraft.mapdata.exception.InvalidSortParameterException;
import com.yogo.metacraft.mapdata.exception.MapDataException;
import com.yogo.metacraft.mapdata.repository.MapDataRepository;
import com.yogo.metacraft.mapdata.service.FirebaseStorageService;
import com.yogo.metacraft.mapdata.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Map Data", description = "맵 데이터 관리 API")
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapDataController {

    private final MapDataRepository mapDataRepository;
    private final FirebaseStorageService firebaseService;
    private final MapService mapService;
    @Autowired
    private ObjectMapper objectMapper;


    @Operation(summary = "MapData 카드 뷰 조회", description = "썸네일과 기본 정보만 포함된 MapData 카드 뷰를 페이징하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("cards")
    public ResponseEntity<CustomApiResponse<PageResponseDto<MapDataCardDto>>> getMapDataCards (
            @Parameter(description = "페이지 번호 ")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 카드 수", example = "12")
            @RequestParam(defaultValue = "12") int size,
            @Parameter(description = "정렬 기준 (mapName, id 등)", example = "맵 이름")
            @RequestParam(defaultValue = "map_name") String sortBy,
            @Parameter(description = "정렬 방향 (ASC, DESC)", example = "asc")
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSortParameterException("Invalid sort direction: " + direction);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<MapData> mapDataPage = mapDataRepository.findAll(pageable);
        Page<MapDataCardDto> cardDtoPage = mapDataPage.map(MapDataCardDto::new);
        PageResponseDto<MapDataCardDto> response = new PageResponseDto<>(cardDtoPage);

        return ResponseEntity.ok(new CustomApiResponse<>(
                true,
                "Map cards retrieved successfully",
                response
        ));
    }

    @Operation(summary = "MapData 카드 뷰 조회 (페이지네이션 없음)", description = "썸네일과 기본 정보만 포함된 MapData 카드 뷰를 페이지네이션 없이 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MapDataCardDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })

    @GetMapping("/cards-nopage")
    public ResponseEntity<SimpleApiResponse<List<MapDataCardDto>>> getMapDataCardsNoPage(
            @Parameter(description = "정렬 기준 (mapName, id 등)", example = "맵 이름")
            @RequestParam(defaultValue = "mapName") String sortBy,
            @Parameter(description = "정렬 방향 (ASC, DESC)", example = "asc")
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        log.info("Received request for cards-nopage");

        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
            Sort sort = Sort.by(sortDirection, sortBy);

            List<MapData> mapDataList = mapDataRepository.findAll(sort);
            List<MapDataCardDto> cardDtos = mapDataList.stream()
                    .map(MapDataCardDto::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new SimpleApiResponse<>(cardDtos));
        } catch (IllegalArgumentException e) {
            throw new InvalidSortParameterException("Invalid sort direction: " + direction);
        }
    }

    @Operation(summary = "모든 MapData 조회", description = "데이터베이스에 저장된 모든 MapData를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/all")
    public ResponseEntity<CustomApiResponse<List<MapData>>> getAllTestData() {
        List<MapData> mapDataList = mapDataRepository.findAll();
        return ResponseEntity.ok(new CustomApiResponse<>(true, "All MapData retrieved successfully", mapDataList));
    }

    @Operation(summary = "썸네일 URL 조회", description = "특정 MapData의 썸네일 URL을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 ID의 MapData를 찾을 수 없음")
    })
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(@PathVariable Long id) {
        return mapDataRepository.findById(id)
                .map(mapData -> ResponseEntity.ok(Map.of("url", mapData.getThumbnail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "MapData 및 이미지 업로드", description = "MapData와 함께 이미지를 업로드하고 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 업로드 및 데이터 저장 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "JSON 파싱 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/upload")
    public ResponseEntity<SimpleApiResponse<MapData>> updateMapAndImage(
            @Parameter(description = "업로드할 썸네일 이미지 파일", required = false)
            @RequestParam(value = "thumbnail", required = false) MultipartFile file,
            @Parameter(description = "JSON 형식의 MapData 정보", required = true)
            @RequestParam("testData") String testDataJson) {
        try {
            MapDataDto mapDataDto = objectMapper.readValue(testDataJson, MapDataDto.class);
            // 변수 및 로직 추가 (variables 처리)
            if (mapDataDto.getVariables() == null) {
                mapDataDto.setVariables(new ArrayList<String>()); // 기본값 설정
            }
            MapData savedMapData = mapService.uploadMapWithImage(file, mapDataDto);
            return ResponseEntity.ok(new SimpleApiResponse<>(savedMapData));
        } catch (MapDataException e) {
            MapData errorData = null; // 또는 에러 정보를 담은 MapData 객체
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SimpleApiResponse<>(errorData));
        } catch (IOException e) {
            MapData errorData = null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SimpleApiResponse<>(errorData));
        } catch (Exception e) {
            MapData errorData = null;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SimpleApiResponse<>(errorData));
        }
    }

    @Operation(summary = "이미지 없는 MapData 저장", description = "이미지 없이 MapData만 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "데이터 저장 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/upload/no-image")
    public ResponseEntity<SimpleApiResponse<MapData>> updateMapWithoutImage(@RequestBody MapDataDto mapDataDto) {
        try {
            if (mapDataDto.getVariables() == null) {
                mapDataDto.setVariables(new ArrayList<String>()); // 기본값 설정
            }
            MapData savedMapData = mapService.uploadMapWithoutImage(mapDataDto);
            return ResponseEntity.ok(new SimpleApiResponse<>(savedMapData));
        } catch (MapDataException e) {
            MapData errorData = null;
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SimpleApiResponse<>(errorData));
        } catch (Exception e) {
            MapData errorData = null;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SimpleApiResponse<>(errorData));
        }
    }

    @Operation(summary = "MapData 이름으로 조회 (쿼리 파라미터)", description = "mapName을 쿼리 파라미터로 제공하여 MapData를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MapData 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 이름의 MapData를 찾을 수 없음")
    })
    @GetMapping("/find")
    public ResponseEntity<?> getMapDataByMapNameQuery(@RequestParam("mapName") String mapName) {
        List<MapData> mapDataList = mapDataRepository.findByMapName(mapName);

        if (mapDataList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomApiResponse(false, "MapData not found for mapName: " + mapName));
        }
        return ResponseEntity.ok(new CustomApiResponse(true, "MapData retrieved successfully", mapDataList));
    }

    @Operation(summary = "MapData 이름으로 조회 (경로 파라미터)", description = "mapName을 경로 파라미터로 제공하여 MapData를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MapData 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 이름의 MapData를 찾을 수 없음")
    })
    @GetMapping("/{mapName}")
    public ResponseEntity<?> getMapDataByMapNamePath(@PathVariable("mapName") String mapName) {
        List<MapData> mapDataList = mapDataRepository.findByMapName(mapName);

        return ResponseEntity.ok(new SimpleApiResponse<>(mapDataList));
    }

    @Operation(summary = "MapData 수정", description = "ID에 해당하는 MapData를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MapData 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 MapData를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<MapData>> updateMapData(
            @PathVariable Long id,
            @RequestBody MapDataDto mapDataDto) {
        Optional<MapData> optionalMapData = mapDataRepository.findById(id);
        if (optionalMapData.isPresent()) {
            MapData mapData = optionalMapData.get();
            mapData.setMapName(mapDataDto.getMapName());
            mapData.setInstanceData(mapDataDto.getInstanceData());

            MapData updatedMapData = mapDataRepository.save(mapData);
            return ResponseEntity.ok(new CustomApiResponse<>(true, "MapData updated successfully", updatedMapData));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomApiResponse<>(false, "MapData not found for ID: " + id));
        }
    }

    @Operation(summary = "MapData 삭제", description = "ID에 해당하는 MapData를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MapData 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 MapData를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteMapData(@PathVariable Long id) {
        Optional<MapData> optionalMapData = mapDataRepository.findById(id);
        if (optionalMapData.isPresent()) {
            mapDataRepository.deleteById(id);
            return ResponseEntity.ok(new CustomApiResponse<>(true, "MapData deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomApiResponse<>(false, "MapData not found for ID: " + id));
        }
    }

    @Operation(summary = "MapData 삭제", description = "mapName에 해당하는 모든 MapData를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MapData 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 이름의 MapData를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @DeleteMapping("/{mapName}")
    public ResponseEntity<CustomApiResponse<Void>> deleteMapDataByName(@PathVariable String mapName) {
        List<MapData> mapDataList = mapDataRepository.findByMapName(mapName);
        if (!mapDataList.isEmpty()) {
            mapDataRepository.deleteAll(mapDataList);
            return ResponseEntity.ok(new CustomApiResponse<>(true, "All MapData with mapName '" + mapName + "' deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomApiResponse<>(false, "No MapData found for mapName: " + mapName));
        }
    }
}