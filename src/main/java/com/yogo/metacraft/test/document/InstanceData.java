package com.yogo.metacraft.test.document;

import lombok.Data;

@Data
public class InstanceData {
    private int modelNum;
    private String mapName;
    private String serialNumber;
    private Position position;
    private Rotation rotation;
    private Scale scale;
    private JsonParent json_Parent;
}