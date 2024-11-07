package com.yogo.metacraft.mapdata.controller;

import com.yogo.metacraft.mapdata.dto.MapDataDto;
import com.yogo.metacraft.mapdata.domain.MapData;
import com.yogo.metacraft.mapdata.service.MapDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/mapData")
@Tag(name = "Map 관리 시스템", description = "맵 데이터 관리하기")
public class MapDataController {

    private final MapDataService mapDataService;

    public MapDataController(MapDataService mapDataService) {
        this.mapDataService = mapDataService;
    }

    @Operation(summary = "특정 맵데이터 가져오기")
    @GetMapping("/{id}")
    public ResponseEntity<MapData> getMapData(@PathVariable Long id) {
        Optional<MapData> mapData = mapDataService.findById(id);
        return mapData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "맵 데이터 생성하기")
    @PostMapping
    public ResponseEntity<MapData> createMapData(@RequestBody MapDataDto mapDataDto) {
        MapData savedMapData = mapDataService.saveMapData(mapDataDto);
        return ResponseEntity.ok(savedMapData);
    }

    @Operation(summary = "기존 데이터 업데이트 하기")
    @PutMapping("/{id}")
    public ResponseEntity<MapData> updateMapData(@PathVariable Long id, @RequestBody MapDataDto mapDataDto) {
        Optional<MapData> existingMapData = mapDataService.findById(id);
        if (existingMapData.isPresent()) {
            MapData updatedMapData = mapDataService.updateMapData(id, mapDataDto);
            return ResponseEntity.ok(updatedMapData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "특정 맵 삭제하기")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapData(@PathVariable Long id) {
        boolean deleted = mapDataService.deleteMapData(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
