package com.yogo.metacraft.mapdata.document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapDataCardDto {
    private String id;
    private String mapName;
    private String thumbnail;

    public MapDataCardDto(MapData mapData) {
        this.id = mapData.getId();
        this.mapName = mapData.getMapName();
        this.thumbnail = mapData.getThumbnail();
    }
}
