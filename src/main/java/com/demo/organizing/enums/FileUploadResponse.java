package com.demo.organizing.enums;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadResponse {
    private Long id;
    private String type;
    private String message;
    private boolean uploadStatus;
    private String downloadUri;
}
