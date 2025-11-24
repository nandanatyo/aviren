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
import io.minio.SetBucketPolicyArgs;
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
    
    /**
     * Upload file dan return HANYA path (bukan full URL)
     * Path format: profile-photos/uuid.jpg
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID() + extension;
            String objectName = folder + "/" + filename;
            
            // Upload file to MinIO
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
            
            // Return HANYA path, bukan full URL
            // Nanti akan di-convert ke full URL saat mapping response
            return objectName; // e.g: "profile-photos/abc-123.jpg"
            
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    /**
     * Generate full URL dari path
     * Path: profile-photos/uuid.jpg -> http://SERVER_BASE_URL/api/files/profile-photos/uuid.jpg
     */
    public String generateFileUrl(String path, String serverBaseUrl) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return String.format("%s/api/files/%s", serverBaseUrl, path);
    }
    
    public MinioFileResponse getFile(String objectName) {
        try {
            // Get object stats
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            
            // Get object stream
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
    
    /**
     * Set bucket policy to public read
     * Call this method once during initialization
     */
    public void setBucketPublic() {
        try {
            String policy = String.format("""
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": {"AWS": "*"},
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/*"]
                        }
                    ]
                }
                """, bucketName);
            
            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policy)
                    .build()
            );
            
            log.info("Bucket '{}' set to public read access", bucketName);
        } catch (Exception e) {
            log.error("Error setting bucket policy", e);
        }
    }
}