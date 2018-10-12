package com.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String main(HttpServletRequest request) {
        return "login";
    }

    @RequestMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username " + username + ", password " + password);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        if ("aa".equals(username) && "vv".equals(password)) {
            writer.write("{\"success\":true}");
        } else {
            writer.write("{\"failed\":true, \"msg\":\"wrong password\"}");
        }
        writer.flush();
        writer.close();
    }
}
