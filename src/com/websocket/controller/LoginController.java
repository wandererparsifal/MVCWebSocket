package com.websocket.controller;

import com.websocket.Constant;
import com.websocket.bean.QueryResult;
import com.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.websocket.Code.CODE_SUCCESS;
import static com.websocket.Constant.KEY_USER;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @ResponseBody
    @RequestMapping("doLogin")
    public QueryResult<Map<String, Object>> doLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        QueryResult<Map<String, Object>> loginResult = userService.login(request.getSession().getServletContext(),
                username, password);
        if (CODE_SUCCESS == loginResult.getCode()) {
            Map<String, Object> payload = loginResult.getPayload();
            request.getSession().setAttribute(Constant.ATTR_HTTP_SESSION_USER, payload.get(KEY_USER));
        }
        return loginResult;
    }
}
