package com.leyou.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ly.user.sms.register")
@Data
public class RegisterSmsProperty {
    private String exchange;
    private String routingKey;
    private Integer expiredTime;
}
