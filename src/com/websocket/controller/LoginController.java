package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username " + username + ", password " + password);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer;
        ResponseBean responseBean;
        try {
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            if ("aa".equals(username) && "vv".equals(password)) {
                responseBean = new ResponseBean(true, false, "");
                String json = mapper.writeValueAsString(responseBean);
                writer.write(json);
            } else {
                responseBean = new ResponseBean(false, true, "wrong password");
                String json = mapper.writeValueAsString(responseBean);
                writer.write(json);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ResponseBean {

        boolean success;

        boolean failed;

        String msg;

        private ResponseBean(boolean success, boolean failed, String msg) {
            this.success = success;
            this.failed = failed;
            this.msg = msg;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isFailed() {
            return failed;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
