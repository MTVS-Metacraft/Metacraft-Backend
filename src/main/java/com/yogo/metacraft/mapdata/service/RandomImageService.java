package com.yogo.metacraft.mapdata.service;


import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.List;

@Service
public class RandomImageService {

    // 사용할 이미지 URL 리스트
    // bucket 이름 확인 필요
    private static final List<String> DEFAULT_IMAGE_URLS = List.of(
            "https://storage.googleapis.com/metacraft-48f9c.firebasestorage.app/sky.jpg",
            "https://storage.googleapis.com/metacraft-48f9c.firebasestorage.app/arrow.jpg",
            "https://storage.googleapis.com/metacraft-48f9c.firebasestorage.app/mountain.jpg",
            "https://storage.googleapis.com/metacraft-48f9c.firebasestorage.app/galaxy.jpg"
    );

    private final Random random = new Random();

    // 무작위로 기본 이미지 URL을 선택하는 메서드
    public String getRandomDefaultImageUrl() {
        int index = random.nextInt(DEFAULT_IMAGE_URLS.size());
        return DEFAULT_IMAGE_URLS.get(index);
    }
}