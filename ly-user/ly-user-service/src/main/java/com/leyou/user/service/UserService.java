package com.leyou.user.service;

import com.google.common.collect.Maps;
import com.leyou.common.enums.LyUserExceptionEnum;
import com.leyou.common.exception.LyUserException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.properties.RegisterSmsProperty;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(value = RegisterSmsProperty.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RegisterSmsProperty registerSmsProperty;

    private static final String KEY_PREFIX = "user:verify:phone";

    public Boolean checkUser(String data, Integer type) {
        //判断数据类型
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyUserException(LyUserExceptionEnum.USER_PARAM_ERROR);
        }
        return userMapper.selectCount(user) == 0;
    }

    public void sendCode(String phone) {
        Map<String, String> msg = Maps.newHashMap();
        msg.put("phone", phone);
        String key = KEY_PREFIX + phone;
        String code = NumberUtils.generateCode(6);
        msg.put("code", code);
        //发送消息  -- rabbit
        amqpTemplate.convertAndSend(registerSmsProperty.getExchange(), registerSmsProperty.getRoutingKey(), msg);
        //保存验证码
        stringRedisTemplate.opsForValue().set(key, code, registerSmsProperty.getExpiredTime(), TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        //1. 校验验证码
        String key = KEY_PREFIX + user.getPassword();
        String codeR = stringRedisTemplate.opsForValue().get(key);

        if(!StringUtils.equals(code, codeR)) {
            return;
        }
        //2. 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //3. 加密密码
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        //4. 新增用户
        user.setId(null);
        user.setCreated(new Date());
        int count = userMapper.insert(user);
        if(count < 1) {
            throw new LyUserException(LyUserExceptionEnum.USER_PARAM_ERROR);
        }
    }

    public User queryUser(String username, String password) {

        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if(user == null) {
            return null;
        }
        String hex = CodecUtils.md5Hex(password, user.getSalt());
        if(StringUtils.equals(hex, user.getPassword())) {
            return user;
        }
        return null;
    }
}