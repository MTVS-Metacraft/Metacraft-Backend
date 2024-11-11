package com.yogo.metacraft.test.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    private final Storage storage;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    // 기존 업로드 메소드
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getInputStream());

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    // 파일을 바이트 배열로 가져오는 메소드
    public byte[] getFileBytes(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);

        if (blob != null) {
            return blob.getContent();
        }
        throw new RuntimeException("File not found: " + fileName);
    }

    // URL에서 파일명 추출하여 바이트 배열로 가져오는 메소드
    public byte[] getBytesFromUrl(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        return getFileBytes(fileName);
    }

    // Firebase Storage URL에서 파일명 추출
    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + getExtension(file.getOriginalFilename());
    }

    private String getExtension(String fileName) {
        return fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
}