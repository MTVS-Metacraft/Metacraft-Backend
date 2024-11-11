package com.yogo.metacraft.mapdata.document;

import lombok.Data;

@Data
public class InstanceData {
    private Double modelNum;
    private String mapName;
    private String serialNumber;
    private Position position;
    private Rotation rotation;
    private Scale scale;
    private JsonParent json_Parent;
}