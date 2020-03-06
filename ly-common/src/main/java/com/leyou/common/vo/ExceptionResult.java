package com.leyou.common.vo;

import com.leyou.common.enums.LyMarketExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(LyMarketExceptionEnum lyMarketExceptionEnum) {
        this.status = lyMarketExceptionEnum.getCode();
        this.message = lyMarketExceptionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
