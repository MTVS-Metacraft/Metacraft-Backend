package com.yogo.metacraft.test.document;

import java.util.List;

public class TestDataDto {
    private String mapName;
    private List<InstanceData> instanceDatas;

    // Getter, Setter
    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public List<InstanceData> getInstanceDatas() {
        return instanceDatas;
    }

    public void setInstanceDatas(List<InstanceData> instanceDatas) {
        this.instanceDatas = instanceDatas;
    }
}