package com.yogo.metacraft.mapData.dto;

import com.yogo.metacraft.mapData.domain.MapObject;
import lombok.Data;

import java.util.List;

@Data
public class MapDataDto {
    private String mapName;
    private List<MapObjectDto> objects;
}