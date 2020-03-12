package com.leyou.gateway.properties;

import com.leyou.auth.common.utils.RsaUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@PropertySource(value = "classpath:application-jwt.yml")
@Configuration
public class JwtProperties {
    @Value("${publicKeyPath}")
    private String publicKeyPath;
    @Value("${cookieName}")
    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
