package com.leyou.search.config;

import com.leyou.common.utils.TransferTToKUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config<T, K> {
    @Bean
    public TransferTToKUtil getTToKUtilBean() {
        return new TransferTToKUtil<T, K>();
    }
}
