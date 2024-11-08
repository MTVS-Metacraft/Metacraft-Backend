// com.yogo.metacraft.test.controller.TestDataController.java
package com.yogo.metacraft.test.controller;

import com.yogo.metacraft.test.document.TestData;
import com.yogo.metacraft.test.repository.TestDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataRepository repository;

    @PostMapping("/save")
    public ResponseEntity<TestData> saveTestData(@RequestBody TestData testData) {
        return ResponseEntity.ok(repository.save(testData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestData> getTestData(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TestData>> getAllTestData() {
        return ResponseEntity.ok(repository.findAll());
    }
}