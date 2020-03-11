package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LyUserExceptionEnum {

    USER_PARAM_ERROR(400, "请求参数有误"),
    USER_INSERT_ERROR(400, "用户创建失败")
    ;
    int code;
    String msg;
}
