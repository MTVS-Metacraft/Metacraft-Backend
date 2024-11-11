package com.yogo.metacraft.mapdata.service;

import com.yogo.metacraft.mapdata.repository.MapDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapDataRepository mapDataRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final SequenceGeneratorService sequenceGeneratorService;

//    public TestData saveMapWithThumbnail(TestData data, MultipartFile thumbnail) throws IOException {
//        // ID 생성
//        if (data.getId() == null) {
//            data.setId(sequenceGenerator.generateSequence(TestData.class.getSimpleName()));
//        }
//
//        // 썸네일 이미지 업로드 및 URL 설정
//        if (thumbnail != null && !thumbnail.isEmpty()) {
//            String imageUrl = firebaseService.uploadFile(thumbnail);
//            data.setThumbnail(imageUrl);
//        }
//
//        return repository.save(data);
//    }
}