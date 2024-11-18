package com.yogo.metacraft.mapdata.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SimpleMapDataCardDto {
    private String id;
    private String mapName;
    private String thumbnail;

    public SimpleMapDataCardDto(MapData mapData) {
        this.id = mapData.getId();
        this.mapName = mapData.getMapName();
        this.thumbnail = mapData.getThumbnail();
    }
}