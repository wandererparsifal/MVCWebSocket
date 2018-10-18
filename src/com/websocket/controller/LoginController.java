package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.Constant;
import com.websocket.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@Controller
public class LoginController {

    @RequestMapping("login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @RequestMapping("doLogin")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String path = request.getSession().getServletContext().getRealPath("/storage/user.json");
        String usersJson = readFile(path);
        ArrayList<User> users = null;
        if (!StringUtils.isEmpty(usersJson)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                users = mapper.readValue(usersJson,
                        mapper.getTypeFactory().constructParametricType(ArrayList.class, User.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer;
        ResponseBean responseBean = null;
        if (null != users) {
            boolean found = false;
            for (User user : users) {
                if (user.getName().equals(username)) {
                    if (user.getPwd().equals(password)) {
                        request.getSession().setAttribute(Constant.ATTR_HTTP_SESSION_USER, user);
                        responseBean = new ResponseBean(true, false, "");
                    } else {
                        responseBean = new ResponseBean(false, true, "Wrong password");
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                responseBean = new ResponseBean(false, true, "No such user");
            }
        } else {
            responseBean = new ResponseBean(false, true, "No such user");
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

    private String readFile(String path) {
        String result = null;
        File file = new File(path);
        if (file.exists()) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                result = byteArrayOutputStream.toString("UTF-8");
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
