package com.mboot.controller;

import com.mboot.annotation.SystemLog;
import com.mboot.enums.LogType;
import com.mboot.result.Result;
import com.mboot.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param response
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/doLogin")
    @SystemLog(description = "User login", type = LogType.LOGIN)
    public Result<String> doLogin(HttpServletResponse response,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        //User login logic, return token
        String token = userService.login(response, username, password);
        return Result.success(token);
    }


}
