package com.leyou.common.exception;

import com.leyou.common.enums.LyUserExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LyUserException extends RuntimeException{
    private LyUserExceptionEnum lyUserExceptionEnum;
}
