package com.leyou.service;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.utils.JwtUtils;
import com.leyou.client.UserClient;
import com.leyou.properties.JwtProperties;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        //1. 根据用户名和密码查询
        User user = userClient.queryUser(username, password);
        //2. 判断user
        if(user == null) {
            return null;
        }
        //3. jwtUtils 生成 jwt-token
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setId(user.getId());
            return JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(),
                    jwtProperties.getExpiredTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
