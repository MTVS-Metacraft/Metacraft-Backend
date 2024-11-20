package com.yogo.metacraft.mapdata.document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MapDataCardDto {
    private String id;
    private String mapName;
    private String userName;
    private String thumbnail;

    public MapDataCardDto(MapData mapData) {
        this.id = mapData.getId();
        this.mapName = mapData.getMapName();
        this.userName = mapData.getUserName();
        this.thumbnail = mapData.getThumbnail();
    }
}
