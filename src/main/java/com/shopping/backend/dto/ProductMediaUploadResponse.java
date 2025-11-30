//UC25
package com.shopping.backend.dto;

import lombok.Data;

@Data
public class ProductMediaUploadResponse {
    private String mediaId;
    private String url;   // local URL or static URL
    private String type;  // image / video
}

