package com.websocket.controller;

import com.websocket.MsgSocketHandler;
import com.websocket.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RequestMapping("websocket")
@Controller
public class UserController {

    private final MsgSocketHandler msgSocketHandler;

    @Autowired
    public UserController(MsgSocketHandler handler) {
        this.msgSocketHandler = handler;
    }

    @RequestMapping("index")
    public String login(User user, HttpServletRequest request) {
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        request.getSession().setAttribute("user", user);
        return "index";
    }

    @ResponseBody
    @RequestMapping("sendMsg")
    public String sendMag(String content) {
        TextMessage textMessage = new TextMessage(content);
        msgSocketHandler.sendMessageToAllUser(textMessage);
        return "200";
    }
}