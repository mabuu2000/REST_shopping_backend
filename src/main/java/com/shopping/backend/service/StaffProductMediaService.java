//UC25

package com.shopping.backend.service;

import com.shopping.backend.dto.ProductMediaUploadResponse;
import com.shopping.backend.model.Product;
import com.shopping.backend.model.ProductMedia;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.ProductMediaRepository;
import com.shopping.backend.repo.ProductRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StaffProductMediaService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMediaRepository mediaRepository;

    @Autowired
    public StaffProductMediaService(UserRepository userRepository,
                                    ProductRepository productRepository,
                                    ProductMediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mediaRepository = mediaRepository;
    }

    private User ensureStaff(String email) {
        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"staff".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Permission denied: Staff only");
        }
        return user;
    }

    /** UC25: Upload media file */
    public ProductMediaUploadResponse uploadMedia(UUID productId, MultipartFile file, String email) {

        ensureStaff(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validation: file must not be empty
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Format validation
        String filename = file.getOriginalFilename();
        if (filename == null) throw new RuntimeException("Invalid file name");

        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        boolean isImage = ext.matches("jpg|jpeg|png|webp");
        boolean isVideo = ext.matches("mp4|mov");

        if (!isImage && !isVideo) {
            throw new RuntimeException("Invalid format. Only jpg, jpeg, png, webp, mp4, mov allowed.");
        }

        // Size limit 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File too large (max 10MB)");
        }

        // Save file locally
        Path uploadDir = Path.of("uploads");
        try { Files.createDirectories(uploadDir); } catch (Exception ignored) {}

        Path filePath = uploadDir.resolve(UUID.randomUUID() + "_" + filename);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file");
        }

        // Save media to DB
        ProductMedia media = new ProductMedia();
        media.setProduct(product);
        media.setType(isImage ? "image" : "video");
        media.setUrl(filePath.toString());

        mediaRepository.save(media);

        // Response
        ProductMediaUploadResponse res = new ProductMediaUploadResponse();
        res.setMediaId(media.getId().toString());
        res.setType(media.getType());
        res.setUrl(media.getUrl());

        return res;
    }
}

