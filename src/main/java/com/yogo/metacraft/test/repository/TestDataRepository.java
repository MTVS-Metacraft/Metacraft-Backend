// com.yogo.metacraft.test.repository.TestDataRepository.java
package com.yogo.metacraft.test.repository;

import com.yogo.metacraft.test.document.TestData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestDataRepository extends MongoRepository<TestData, String> {
    // 기본 CRUD 작업은 MongoRepository에서 제공
    // 필요한 경우 추가 쿼리 메서드 정의 가능
}