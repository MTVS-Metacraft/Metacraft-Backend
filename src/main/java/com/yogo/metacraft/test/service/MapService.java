package com.yogo.metacraft.test.service;

import com.yogo.metacraft.test.document.TestData;
import com.yogo.metacraft.test.repository.TestDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final TestDataRepository repository;
    private final FirebaseStorageService firebaseService;
    private final SequenceGeneratorService sequenceGenerator;

    public TestData saveMapWithThumbnail(TestData data, MultipartFile thumbnail) throws IOException {
        // ID 생성
        if (data.getId() == null) {
            data.setId(sequenceGenerator.generateSequence(TestData.class.getSimpleName()));
        }

        // 썸네일 이미지 업로드 및 URL 설정
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String imageUrl = firebaseService.uploadFile(thumbnail);
            data.setThumbnail(imageUrl);
        }

        return repository.save(data);
    }
}