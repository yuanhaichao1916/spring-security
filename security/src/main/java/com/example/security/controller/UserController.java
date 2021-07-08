package com.example.security.controller;

import com.example.security.entry.Users;
import com.example.security.enums.ResultEnum;
import com.example.security.pojo.SimpleResponse;
import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RestController("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * This function is use to user login.
     *
     * @param mobileNo     mobileNo
     * @param password     password
     * @param inputCaptcha inputCaptcha
     * @param session      session
     * @param response     response
     * @return
     */
    @PostMapping(value = "/session", produces = {"application/json;charset=UTHF-8"})
    public SimpleResponse login(String mobileNo, String password, String inputCaptcha, HttpSession session, HttpServletResponse response) {
        // Determine whether the verification code is correct
        //判断验证码是否正确
        Users user = userService.userLogin(mobileNo, password);
        if (user != null) {
            /*设置自动登陆，一个星期.  将token保存在数据库中*/
            return SimpleResponse.success(user);
        } else {
            return SimpleResponse.error(ResultEnum.LOGIN_ERROR);
        }

    }

    @DeleteMapping(value = "/session", produces = {"application/json;charset=UTF-8"})
    public SimpleResponse logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        // delete session and cookie
        session.removeAttribute("user");

//        CookieUtil.clear

        return SimpleResponse.success();
    }

}
