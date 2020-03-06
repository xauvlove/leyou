package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LyCodeExceptionEnum {

    USE_TRANSFERTTOK_NOARGCONSTRUCTOR_ERROR(1, "没有无参构造方法"),
    ;
    int code;
    String msg;
}
