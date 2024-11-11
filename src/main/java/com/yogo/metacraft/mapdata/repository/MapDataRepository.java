package com.yogo.metacraft.mapdata.repository;

import com.yogo.metacraft.mapdata.document.MapData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapDataRepository extends MongoRepository<MapData, Long> {
    // 기본 CRUD 작업은 MongoRepository에서 제공
    // 필요한 경우 추가 쿼리 메서드 정의 가능
}