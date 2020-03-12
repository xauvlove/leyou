package com.leyou.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "ly.filter")
public class WhiteListProperties {
    private List<String> allowPaths;
}
