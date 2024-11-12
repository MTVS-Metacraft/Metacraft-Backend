package com.yogo.metacraft.mapdata.document;

import lombok.Data;

@Data
public class InstanceData {
    private Integer modelNum;
    private String serialNumber;
    private Position position;
    private Rotation rotation;
    private Scale scale;
    private JsonParent json_Parent;
}