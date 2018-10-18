package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.Constant;
import com.websocket.bean.LoginResult;
import com.websocket.bean.ResponseBean;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @RequestMapping("doLogin")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer;
        ResponseBean responseBean;
        LoginResult loginResult = userService.login(request.getSession().getServletContext(),
                username, password);
        if (loginResult.isSuccess()) {
            request.getSession().setAttribute(Constant.ATTR_HTTP_SESSION_USER, loginResult.getUser());
            responseBean = new ResponseBean(true, false, loginResult.getToken());
        } else {
            switch (loginResult.getErrorCode()) {
                case 301:
                    responseBean = new ResponseBean(false, true, "No such user");
                    break;
                case 302:
                    responseBean = new ResponseBean(false, true, "Wrong password");
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
