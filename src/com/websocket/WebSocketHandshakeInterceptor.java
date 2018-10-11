package com.websocket;

import com.websocket.bean.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 在握手之前执行该方法, 继续握手返回 true, 中断握手返回 false。通过 attributes 参数设置 WebSocketSession 的属性
     *
     * @param request
     * @param response
     * @param webSocketHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpSession httpSession = servletServerHttpRequest.getServletRequest().getSession(false);
            if (httpSession != null) {
                // 从 HttpSession 中获取当前用户
                User user = (User) httpSession.getAttribute(Constant.ATTR_HTTP_SESSION_USER);
                // 放到 WebSocketSession 中
                attributes.put(Constant.ATTR_WEBSOCKET_SESSION_USER, user);
            }
        }
        return true;
    }

    /**
     * 在握手之后执行该方法
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}