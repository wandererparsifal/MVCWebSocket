package com.websocket;

import com.websocket.bean.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class MsgSocketHandler implements WebSocketHandler {

    /**
     * 已经连接的用户
     */
    private static final ArrayList<WebSocketSession> users;

    static {
        //保存当前连接用户
        users = new ArrayList<>();
    }

    /**
     * 建立链接
     *
     * @param webSocketSession
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        //将用户信息添加到list中
        users.add(webSocketSession);
        System.out.println("=====================建立连接成功==========================");
        User user = (User) webSocketSession.getAttributes().get(Constant.ATTR_WEBSOCKET_SESSION_USER);
        if (user != null) {
            System.out.println("当前连接用户======" + user.getName());
        }
        System.out.println("webSocket连接数量=====" + users.size());
    }

    /**
     * 接收消息
     *
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        User user = (User) webSocketSession.getAttributes().get(Constant.ATTR_WEBSOCKET_SESSION_USER);
        System.out.println("收到用户:" + user.getName() + "的消息");
        System.out.println(webSocketMessage.getPayload().toString());
        System.out.println("===========================================");
    }

    /**
     * 异常处理
     *
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        if (webSocketSession.isOpen()) {
            //关闭session
            try {
                webSocketSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //移除用户
        users.remove(webSocketSession);
    }

    /**
     * 断开链接
     *
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        users.remove(webSocketSession);
        User user = (User) webSocketSession.getAttributes().get(Constant.ATTR_WEBSOCKET_SESSION_USER);
        System.out.println(user.getName() + "断开连接");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送消息给所有的用户
     *
     * @param messageInfo
     */
    public void sendMessageToAllUser(TextMessage messageInfo) {
        for (WebSocketSession session : users) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(messageInfo);
                    System.out.println("内容：" + messageInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}