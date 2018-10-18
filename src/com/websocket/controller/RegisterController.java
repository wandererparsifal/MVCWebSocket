package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.bean.RegisterResult;
import com.websocket.bean.ResponseBean;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.websocket.bean.RegisterResult.CODE_STORAGE_ERROR;
import static com.websocket.bean.RegisterResult.CODE_USER_ALREADY_EXISTS;

@Controller
public class RegisterController {

    @Resource
    private UserService userService;

    @RequestMapping("register")
    public String register(HttpServletRequest request) {
        return "register";
    }

    @RequestMapping("doRegister")
    public void doRegister(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer;
        ResponseBean responseBean;
        RegisterResult registerResult = userService.register(request.getSession().getServletContext(),
                username, password);
        if (registerResult.isSuccess()) {
            responseBean = new ResponseBean(true, false, "");
        } else {
            switch (registerResult.getErrorCode()) {
                case CODE_USER_ALREADY_EXISTS:
                    responseBean = new ResponseBean(false, true, "user already exists");
                    break;
                case CODE_STORAGE_ERROR:
                    responseBean = new ResponseBean(false, true, "storage error");
                    break;
                default:
                    responseBean = new ResponseBean(false, true, "unknown error");
                    break;
            }
        }
        try {
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(responseBean);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
