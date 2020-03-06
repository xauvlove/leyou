package com.leyou.common.exception;

import com.leyou.common.enums.LyCodeExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LyCodeException extends RuntimeException{
    private LyCodeExceptionEnum lyCodeExceptionEnum;
}
