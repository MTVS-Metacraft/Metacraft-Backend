package com.yogo.metacraft.mapdata.dto;

import lombok.Data;

import java.util.List;

@Data
public class MapDataDto {
    private String mapName;
    private List<MapObjectDto> objects;
}