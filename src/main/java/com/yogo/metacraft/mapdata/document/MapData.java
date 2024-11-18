package com.yogo.metacraft.mapdata.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "map_data")  // MongoDB 컬렉션 이름 지정
public class MapData {
    @Id
    private String id;  // MongoDB의 자동생성 ID
    private String mapName;
    private String thumbnail;  // Firebase Storage URL

    @JsonProperty("InstanceDatas")
    private List<InstanceData> instanceData;
}
