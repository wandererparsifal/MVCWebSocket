package com.websocket.controller;

import com.websocket.bean.QueryResult;
import com.websocket.bean.User;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    @Resource
    private UserService userService;

    @RequestMapping("register")
    public String register(HttpServletRequest request) {
        return "register";
    }

    @ResponseBody
    @RequestMapping("doRegister")
    public QueryResult<User> doRegister(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return userService.register(request.getSession().getServletContext(),
                username, password);
    }
}
