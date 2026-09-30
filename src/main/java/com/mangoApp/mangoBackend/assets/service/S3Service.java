package com.mangoApp.mangoBackend.assets.service;

import com.mangoApp.mangoBackend.assets.model.UploadImageResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.util.Base64;

@Service
public class S3Service {

    private S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String regionString;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(regionString))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public UploadImageResponseDTO uploadFile(String keyName, String base64Data) {
        try {
            String cleanBase64 = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64);

            if (decodedBytes.length > 5 * 1024 * 1024) { // 5MB limit
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Image size exceeds 5MB limit");
            }

            // Adivinar el content type básico si es posible (opcional)
            String contentType = "image/jpeg";
            if (base64Data.startsWith("data:image/png")) contentType = "image/png";
            else if (base64Data.startsWith("data:image/webp")) contentType = "image/webp";

            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putOb, RequestBody.fromBytes(decodedBytes));

            // Construir la URL pública manualmente
            String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, regionString, keyName);
            
            return new UploadImageResponseDTO(url);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error uploading to S3: " + e.getMessage());
        }
    }
}
