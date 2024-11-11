package com.yogo.metacraft.test.document;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "test_data")  // MongoDB 컬렉션 이름 지정
public class TestData {
    @Id
    private String id;  // MongoDB의 자동생성 ID
    private String mapName;
    private String thumbnail;  // Firebase Storage URL
    private List<InstanceData> instanceData;
}
