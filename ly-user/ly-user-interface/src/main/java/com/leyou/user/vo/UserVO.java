package com.leyou.user.vo;

import lombok.Data;
import java.util.Date;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private Date created;
}
