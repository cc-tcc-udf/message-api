package org.campus.connect.message.files.FileCloud;

import lombok.*;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CloudinaryUploadDTO {
  private String publicId;
  private String version;
  private String signature;
  private String width;
  private String height;
  private String format;
  private String resourceType;
  private String createdAt;
  private String tags;
  private String bytes;
  private String type;
  private String etag;
  private String url;
  private String secureUrl;
  private String originalFilename;

  public CloudinaryUploadDTO(Map<String, Object> uploadResult) {
    this.publicId = (String) uploadResult.get("public_id");
    this.version = String.valueOf(uploadResult.get("version"));
    this.signature = (String) uploadResult.get("signature");
    this.width = String.valueOf(uploadResult.get("width"));
    this.height = String.valueOf(uploadResult.get("height"));
    this.format = (String) uploadResult.get("format");
    this.resourceType = (String) uploadResult.get("resource_type");
    this.createdAt = (String) uploadResult.get("created_at");
    this.tags = String.valueOf(uploadResult.get("tags"));
    this.bytes = String.valueOf(uploadResult.get("bytes"));
    this.type = (String) uploadResult.get("type");
    this.etag = (String) uploadResult.get("etag");
    this.url = (String) uploadResult.get("url");
    this.secureUrl = (String) uploadResult.get("secure_url");
    this.originalFilename = (String) uploadResult.get("original_filename");
  }
}
