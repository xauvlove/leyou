package com.leyou.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ly.sms")
@Data
public class SmsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String verifyCodeTemplate;
}
