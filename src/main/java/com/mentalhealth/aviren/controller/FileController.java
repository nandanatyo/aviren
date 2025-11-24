package com.mentalhealth.aviren.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mentalhealth.aviren.service.MinioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    
    private final MinioService minioService;
    
    @GetMapping("/{folder}/{filename}")
    public ResponseEntity<InputStreamResource> getFile(
            @PathVariable String folder,
            @PathVariable String filename) {
        
        try {
            String objectName = folder + "/" + filename;
            var fileStream = minioService.getFile(objectName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileStream.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(new InputStreamResource(fileStream.getInputStream()));
                    
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}