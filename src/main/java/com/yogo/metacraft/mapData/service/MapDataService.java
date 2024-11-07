package com.yogo.metacraft.mapData.service;

import com.yogo.metacraft.mapData.dto.MapDataDto;
import com.yogo.metacraft.mapData.dto.MapObjectDto;
import com.yogo.metacraft.mapData.domain.MapData;
import com.yogo.metacraft.mapData.domain.MapObject;
import com.yogo.metacraft.mapData.domain.PositionVector;
import com.yogo.metacraft.mapData.domain.ScaleVector;
import com.yogo.metacraft.mapData.domain.Quaternion;
import com.yogo.metacraft.mapData.repository.MapDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MapDataService {

    @Autowired
    private MapDataRepository mapDataRepository;

    public MapData saveMapData(MapDataDto mapDataDto) {
        MapData mapData = convertToMapDataEntity(mapDataDto);
        return mapDataRepository.save(mapData);
    }

    private MapData convertToMapDataEntity(MapDataDto mapDataDto) {
        List<MapObject> mapObjects = mapDataDto.getObjects().stream()
                .map(this::convertToMapObjectEntity)
                .collect(Collectors.toList());

        return MapData.builder()
                .mapName(mapDataDto.getMapName())
                .objects(mapObjects) // 변환된 MapObject 리스트를 설정
                .build();
    }

    private MapObject convertToMapObjectEntity(MapObjectDto objectDto) {
        PositionVector position = new PositionVector(objectDto.getPosition().getX(), objectDto.getPosition().getY(), objectDto.getPosition().getZ());
        Quaternion rotation = new Quaternion(objectDto.getRotation().getX(), objectDto.getRotation().getY(), objectDto.getRotation().getZ(), objectDto.getRotation().getW());
        ScaleVector scale = new ScaleVector(objectDto.getScale().getX(), objectDto.getScale().getY(), objectDto.getScale().getZ());

        return MapObject.builder()
                .objectName(objectDto.getObjectName())
                .prefabName(objectDto.getPrefabName())
                .position(position)
                .rotation(rotation)
                .scale(scale)
                .build();
    }

    public Optional<MapData> findById(Long id) {
        return mapDataRepository.findById(id);
    }

    public MapData updateMapData(Long id, MapDataDto mapDataDto) {
        return mapDataRepository.findById(id).map(existingMapData -> {
                    List<MapObject> updatedObjects = mapDataDto.getObjects().stream()
                            .map(this::convertToMapObjectEntity)
                            .collect(Collectors.toList());

                    return MapData.builder()
                            .id(existingMapData.getId()) // ID를 유지하여 기존 데이터 갱신
                            .mapName(mapDataDto.getMapName())
                            .objects(updatedObjects)
                            .build();
                }).map(mapDataRepository::save)
                .orElseThrow(() -> new RuntimeException("MapData not found for id: " + id));
    }

    public boolean deleteMapData(Long id) {
        if (mapDataRepository.existsById(id)) {
            mapDataRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
