package com.yogo.metacraft.test;

import com.yogo.metacraft.test.document.InstanceData;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "map_data")
@Data
public class MapData {
    @Id
    private String id;
    private List<InstanceData> InstanceDatas;
}
