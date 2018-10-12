package com.websocket.controller;

import com.websocket.Constant;
import com.websocket.MsgSocketHandler;
import com.websocket.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class ChatController {

    private final MsgSocketHandler msgSocketHandler;

    @Autowired
    public ChatController(MsgSocketHandler handler) {
        this.msgSocketHandler = handler;
    }

    @RequestMapping("chat")
    public String index(HttpServletRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        request.getSession().setAttribute(Constant.ATTR_HTTP_SESSION_USER, user);
        return "chat";
    }

    @ResponseBody
    @RequestMapping("sendMsg")
    public String sendMag(String content) {
        TextMessage textMessage = new TextMessage(content);
        msgSocketHandler.sendMessageToAllUser(textMessage);
        return "200";
    }
}