package com.mangoApp.mangoBackend.assets.model;

public class UploadImageRequestDTO {
    private String productReference;
    private String filename;
    private String base64;

    public String getProductReference() { return productReference; }
    public void setProductReference(String productReference) { this.productReference = productReference; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getBase64() { return base64; }
    public void setBase64(String base64) { this.base64 = base64; }
}
