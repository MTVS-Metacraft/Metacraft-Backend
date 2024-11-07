package com.yogo.metacraft.mapData.dto;

import com.yogo.metacraft.mapData.domain.PositionVector;
import com.yogo.metacraft.mapData.domain.Quaternion;
import com.yogo.metacraft.mapData.domain.ScaleVector;
import com.yogo.metacraft.mapData.domain.Vector;
import lombok.*;

@Data
public class MapObjectDto {
    private String objectName;
    // Prefab은 게임 개발에서 미리 설정된 오브젝트의 템플릿으로, 동일한 오브젝트를 여러 번 재사용할 수 있도록 해주는 기능
    private String prefabName;
    private PositionVector position;
    private Quaternion rotation;
    private ScaleVector scale;
}