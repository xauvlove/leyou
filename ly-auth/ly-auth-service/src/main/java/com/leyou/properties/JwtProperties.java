package com.leyou.properties;

import com.leyou.auth.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
@Data
@PropertySource("classpath:application-jwt.yml")
//@ConfigurationProperties(prefix = "ly.jwt")
@Slf4j
public class JwtProperties {
    @Value("${secret}")
    private String secret;
    @Value("${publicKeyPath}")
    private String publicKeyPath;
    @Value("${privateKeyPath}")
    private String privateKeyPath;
    @Value("${expiredTime}")
    private Integer expiredTime;
    @Value("${cookieName}")
    private String cookieName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 构造方法执行后执行该方法
     */
    @PostConstruct
    public void init() {
        try {
            System.out.println(publicKeyPath);
            System.out.println(privateKeyPath);
            File pubKey = new File(publicKeyPath);
            File priKey = new File(privateKeyPath);
            if(!pubKey.exists() || !priKey.exists()) {
                RsaUtils.generateKey(publicKeyPath, privateKeyPath, secret);
            }
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            log.error("产生公钥和密钥失败！");
            e.printStackTrace();
        }
    }
}
