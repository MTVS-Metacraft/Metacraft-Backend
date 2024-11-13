package com.yogo.metacraft.mapdata.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.List;

@Service
public class RandomImageService {

    // 사용할 이미지 URL 리스트
    // bucket 이름 확인 필요
    @Value("${firebase.bucket-name}")
    private static String bucketName;

    public final List<String> DEFAULT_IMAGE_URLS = List.of(
            "https://storage.googleapis.com/" + bucketName + "/sky.jpg",
            "https://storage.googleapis.com/" + bucketName + "/arrow.jpg",
            "https://storage.googleapis.com/" + bucketName + "/mountain.jpg",
            "https://storage.googleapis.com/" + bucketName + "/galaxy.jpg"
    );

    private final Random random = new Random();

    // 무작위로 기본 이미지 URL을 선택하는 메서드
    public String getRandomDefaultImageUrl() {
        int index = random.nextInt(DEFAULT_IMAGE_URLS.size());
        return DEFAULT_IMAGE_URLS.get(index);
    }
}