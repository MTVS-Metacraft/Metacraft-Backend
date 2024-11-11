package com.yogo.metacraft.mapdata.document;

import java.util.List;

public class MapDataDto {
    private String mapName;
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