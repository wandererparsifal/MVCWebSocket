package com.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.MsgSocketHandler;
import com.websocket.bean.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    private final MsgSocketHandler msgSocketHandler;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ChatController(MsgSocketHandler handler) {
        this.msgSocketHandler = handler;
    }

    @RequestMapping("chat")
    public String index(HttpServletRequest request) {
        return "chat";
    }

    @ResponseBody
    @RequestMapping("sendMsg")
    public String sendMsg(String content, String fromUserName) {
        System.out.println("Message fromUser " + fromUserName);
        Message message = new Message(fromUserName, content, mDateFormat.format(new Date()));
        ObjectMapper mapper = new ObjectMapper();
        String messageString = "";
        try {
            messageString = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        TextMessage textMessage = new TextMessage(messageString);
        msgSocketHandler.sendMessageToAllUser(textMessage);
        return "200";
    }
}