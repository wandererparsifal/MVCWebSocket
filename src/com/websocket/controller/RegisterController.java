package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.bean.QueryResult;
import com.websocket.bean.ResponseInfo;
import com.websocket.bean.User;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.websocket.Code.*;

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
        ResponseInfo responseInfo;
        QueryResult<User> registerResult = userService.register(request.getSession().getServletContext(),
                username, password);
        if (CODE_SUCCESS == registerResult.getCode()) {
            responseInfo = new ResponseInfo(true, false, "");
        } else {
            switch (registerResult.getCode()) {
                case CODE_USER_ALREADY_EXISTS:
                    responseInfo = new ResponseInfo(false, true, "user already exists");
                    break;
                case CODE_STORAGE_ERROR:
                    responseInfo = new ResponseInfo(false, true, "storage error");
                    break;
                default:
                    responseInfo = new ResponseInfo(false, true, "unknown error");
                    break;
            }
        }
        try {
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(responseInfo);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
