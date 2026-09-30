package com.mangoApp.mangoBackend.assets.controller;

import com.mangoApp.mangoBackend.assets.model.UploadImageRequestDTO;
import com.mangoApp.mangoBackend.assets.model.UploadImageResponseDTO;
import com.mangoApp.mangoBackend.assets.service.S3Service;
import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final S3Service s3Service;

    public AssetController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/image/upload")
    public ResponseEntity<UploadImageResponseDTO> uploadImage(@RequestBody UploadImageRequestDTO request) {
        System.out.println("✅ [AssetController] Request recibido para subir imagen: " + request.getFilename());
        
        try {
            Long tenantId = SecurityUtils.getCurrentTenantId();
            Long userId = SecurityUtils.getCurrentUserId();
            
            String keyName = "tenants/" + tenantId + "/users/" + userId + "/products/" + request.getProductReference() + "/" + request.getFilename();
            System.out.println("✅ [AssetController] Subiendo a S3 con key: " + keyName);
            
            UploadImageResponseDTO body = s3Service.uploadFile(keyName, request.getBase64());
            System.out.println("✅ [AssetController] Subida exitosa! URL: " + body.getUrl());
            
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ [AssetController] ERROR FATAL DURANTE LA SUBIDA: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
