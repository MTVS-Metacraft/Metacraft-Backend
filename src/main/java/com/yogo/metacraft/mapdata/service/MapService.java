package com.yogo.metacraft.mapdata.service;

import com.yogo.metacraft.mapdata.document.MapData;
import com.yogo.metacraft.mapdata.document.MapDataDto;
import com.yogo.metacraft.mapdata.exception.MapDataException;
import com.yogo.metacraft.mapdata.repository.MapDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapDataRepository mapDataRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public MapData uploadMapWithImage(MultipartFile file, MapDataDto mapDataDto) throws IOException {
        // mapName 중복 검사
        if (mapDataRepository.existsByMapName(mapDataDto.getMapName())) {
            throw new MapDataException("Map name '" + mapDataDto.getMapName() + "' already exists");
        }

        String imageUrl = firebaseStorageService.uploadFile(file);

        MapData mapData = new MapData();
        mapData.setMapName(mapDataDto.getMapName());
        mapData.setUserName(mapDataDto.getUserName());
        mapData.setInstanceData(mapDataDto.getInstanceData());
        mapData.setPrefabData(mapDataDto.getPrefabData());
        mapData.setVariables(mapDataDto.getVariables());
        mapData.setThumbnail(imageUrl);

        return mapDataRepository.save(mapData);
    }

    public MapData uploadMapWithoutImage(MapDataDto mapDataDto) throws IOException {
        // mapName 중복 검사
        if (mapDataRepository.existsByMapName(mapDataDto.getMapName())) {
            throw new MapDataException("Map name '" + mapDataDto.getMapName() + "' already exists");
        }

        MultipartFile file = null;
        String imageUrl = firebaseStorageService.uploadFile(file);

        MapData mapData = new MapData();
        mapData.setMapName(mapDataDto.getMapName());
        mapData.setUserName(mapDataDto.getUserName());
        mapData.setInstanceData(mapDataDto.getInstanceData());
        mapData.setPrefabData(mapDataDto.getPrefabData());
        mapData.setVariables(mapDataDto.getVariables());
        mapData.setThumbnail(imageUrl);

        return mapDataRepository.save(mapData);
    }
}
