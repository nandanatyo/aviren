package com.mentalhealth.aviren.dto.response;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinioFileResponse {
    private InputStream inputStream;
    private String contentType;
    private long size;
}