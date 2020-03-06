package com.leyou.com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseAccessUrl;
    private List<String> allowTypes;
    private String baseImageStoredPath;
}
