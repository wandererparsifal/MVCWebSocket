package com.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.Constant;
import com.websocket.bean.QueryResult;
import com.websocket.bean.ResponseInfo;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static com.websocket.Code.*;
import static com.websocket.Constant.KEY_TOKEN;
import static com.websocket.Constant.KEY_USER;

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
        ResponseInfo responseInfo;
        QueryResult<Map<String, Object>> loginResult = userService.login(request.getSession().getServletContext(),
                username, password);
        if (CODE_SUCCESS == loginResult.getCode()) {
            Map<String, Object> payload = loginResult.getPayload();
            request.getSession().setAttribute(Constant.ATTR_HTTP_SESSION_USER, payload.get(KEY_USER));
            responseInfo = new ResponseInfo(true, false, (String) payload.get(KEY_TOKEN));
        } else {
            switch (loginResult.getCode()) {
                case CODE_NO_SUCH_USER:
                    responseInfo = new ResponseInfo(false, true, "No such user");
                    break;
                case CODE_WRONG_PASSWORD:
                    responseInfo = new ResponseInfo(false, true, "Wrong password");
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
