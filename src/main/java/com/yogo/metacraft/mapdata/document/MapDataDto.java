package com.yogo.metacraft.mapdata.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MapDataDto {
    private String mapName;
    private String userName;
    @JsonProperty("InstanceDatas")
    private List<InstanceData> instanceData;
}