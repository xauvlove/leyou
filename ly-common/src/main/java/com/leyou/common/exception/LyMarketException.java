package com.leyou.common.exception;

import com.leyou.common.enums.LyMarketExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyMarketException extends RuntimeException{
    private LyMarketExceptionEnum lyMarketExceptionEnum;
}
