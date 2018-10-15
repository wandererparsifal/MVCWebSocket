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
import java.nio.charset.StandardCharsets;
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
    @RequestMapping(value = "sendMsg", produces = {"text/html;charset=UTF-8;", "application/json;"})
    public String sendMsg(String content, String fromUserName) {
        String contentResetCode = new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("Message contentResetCode " + contentResetCode);
        String fromUserNameResetCode = new String(fromUserName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("Message fromUserNameResetCode " + fromUserNameResetCode);
        Message message = new Message(fromUserNameResetCode, contentResetCode, mDateFormat.format(new Date()));
        ObjectMapper mapper = new ObjectMapper();
        String messageString = "";
        try {
            messageString = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("messageString " + messageString);
        TextMessage textMessage = new TextMessage(messageString);
        msgSocketHandler.sendMessageToAllUser(textMessage);
        return "200";
    }
}