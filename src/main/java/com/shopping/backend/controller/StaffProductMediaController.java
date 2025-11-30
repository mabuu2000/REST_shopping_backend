//UC25

package com.shopping.backend.controller;

import com.shopping.backend.dto.ProductMediaUploadResponse;
import com.shopping.backend.service.StaffProductMediaService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff/products")
public class StaffProductMediaController {

    private final StaffProductMediaService service;

    @Autowired
    public StaffProductMediaController(StaffProductMediaService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/{productId}/media",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadMedia(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        ProductMediaUploadResponse res =
                service.uploadMedia(productId, file, email);

        return ResponseEntity.ok(res);
    }
}


