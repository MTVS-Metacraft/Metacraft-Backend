package com.yogo.metacraft.test.controller;

import com.yogo.metacraft.test.document.TestData;
import com.yogo.metacraft.test.repository.TestDataRepository;
import com.yogo.metacraft.test.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataRepository repository;
    private final FirebaseStorageService firebaseService;

    @PostMapping("/save")
    public ResponseEntity<TestData> saveTestData(@RequestBody TestData testData) {
        return ResponseEntity.ok(repository.save(testData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestData> getTestData(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TestData>> getAllTestData() {
        return ResponseEntity.ok(repository.findAll());
    }

    // 썸네일 URL 조회
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(@PathVariable Long id) {
        return repository.findById(id)
                .map(testData -> ResponseEntity.ok(Map.of("url", testData.getThumbnail())))
                .orElse(ResponseEntity.notFound().build());
    }
}