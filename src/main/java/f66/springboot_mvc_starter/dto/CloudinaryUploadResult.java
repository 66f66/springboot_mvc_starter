package f66.springboot_mvc_starter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudinaryUploadResult {

    @JsonProperty("public_id")
    private String publicId;

    @JsonProperty("secure_url")
    private String url;
}
