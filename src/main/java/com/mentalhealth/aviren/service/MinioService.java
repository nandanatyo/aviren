package com.mentalhealth.aviren.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mentalhealth.aviren.dto.response.MinioFileResponse;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    
    private final MinioClient minioClient;
    
    @Value("${minio.bucket-name}")
    private String bucketName;
    
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID() + extension;
            String objectName = folder + "/" + filename;
            
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            
            log.info("File uploaded successfully: {}", objectName);
            
            return objectName;
            
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    public String generateFileUrl(String path, String serverBaseUrl) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return String.format("%s/api/files/%s", serverBaseUrl, path);
    }
    
    public MinioFileResponse getFile(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            
            return new MinioFileResponse(stream, stat.contentType(), stat.size());
            
        } catch (Exception e) {
            log.error("Error getting file from MinIO: {}", objectName, e);
            throw new RuntimeException("Failed to get file", e);
        }
    }
}