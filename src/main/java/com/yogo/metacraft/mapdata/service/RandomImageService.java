package com.yogo.metacraft.mapdata.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RandomImageService {

    @Value("${firebase.bucket-name}")
    private String bucketName;

    private List<String> defaultImageUrls;
    private final Random random = new Random();

    // 생성자나 초기화 블록에서 URL 리스트를 동적으로 설정
    @PostConstruct
    public void initDefaultImageUrls() {
        defaultImageUrls = List.of(
                "https://storage.googleapis.com/" + bucketName + "/sky.jpg",
                "https://storage.googleapis.com/" + bucketName + "/arrow.jpg",
                "https://storage.googleapis.com/" + bucketName + "/mountain.jpg",
                "https://storage.googleapis.com/" + bucketName + "/galaxy.jpg"
        );
    }

    // 무작위로 기본 이미지 URL을 선택하는 메서드
    public String getRandomDefaultImageUrl() {
        int index = random.nextInt(defaultImageUrls.size());
        return defaultImageUrls.get(index);
    }
}