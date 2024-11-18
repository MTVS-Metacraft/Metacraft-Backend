package com.yogo.metacraft.mapdata.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

@ToString
public class MapDataDto {
    private String mapName;
    @JsonProperty("InstanceDatas")
    private List<InstanceData> instanceData;

    // Getter, Setter
    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public List<InstanceData> getInstanceData() {
        return instanceData;
    }

    public void setInstanceData(List<InstanceData> instanceData) {
        this.instanceData = instanceData;
    }
}