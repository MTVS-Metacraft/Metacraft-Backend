package com.yogo.metacraft.test.document;

import java.util.List;

public class TestDataDto {
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

    public void setInstanceDatas(List<InstanceData> instanceData) {
        this.instanceData = instanceData;
    }
}