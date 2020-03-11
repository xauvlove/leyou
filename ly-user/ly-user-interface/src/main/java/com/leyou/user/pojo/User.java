package com.leyou.user.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Length(min = 8, max = 12, message = "用户名必须在8-12位之间")
    private String username;
    @Length(min = 8, max = 12, message = "密码必须在8-12位之间")
    private String password;
    @Pattern(regexp = "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2,5,6,7]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = "手机号不合法")
    private String phone;
    private Date created;
    private String salt;
}
