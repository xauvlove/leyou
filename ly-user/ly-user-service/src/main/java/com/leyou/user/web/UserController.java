package com.leyou.user.web;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 校验数据是否唯一  用户名是否唯一等
     * type 决定了检验什么东西
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(
            @PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        return ResponseEntity.ok(userService.checkUser(data, type));
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone) {
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     *
     * @param user 前端以 form 表单提交，不是json  因此不需要加 Requestbody
     * @Valid 校验用户名等的合法性，传入参数时就校验
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        User user = userService.queryUser(username, password);
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }


}
