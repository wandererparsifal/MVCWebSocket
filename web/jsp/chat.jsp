<%@ page import="com.websocket.bean.User" %><%--
  Created by IntelliJ IDEA.
  User: yangming
  Date: 18-10-10
  Time: 下午2:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
    User user = (User) pageContext.getSession().getAttribute("http-user");
    String name = user.getName();
%>
<html>
<head>
    <title>Title</title>
</head>
<link rel="stylesheet" href="../css/style.css">
<script src="../js/jquery-3.3.1.min.js"></script>
<script src="../js/sockjs.min.js"></script>
<script>
  $(document).ready(function () {
    var ws;
    if ('WebSocket' in window) {
      ws = new WebSocket("ws://" + window.location.host + "/webSocketServer");
    } else if ('MozWebSocket' in window) {
      ws = new MozWebSocket("ws://" + window.location.host + "/webSocketServer");
    } else {
      //如果是低版本的浏览器，则用SockJS这个对象，对应了后台“sockjs/webSocketServer”这个注册器，
      //它就是用来兼容低版本浏览器的
      ws = new SockJS("http://" + window.location.host + "/sockjs/webSocketServer");
    }
    ws.onopen = function (evnt) {
    };
    //接收到消息
    ws.onmessage = function (evnt) {
      console.log(evnt.data);
      var message = JSON.parse(evnt.data);
      var chatList = $("#chatList");
      if ("<%=user.getName()%>" === message.fromWho) {
        chatList.append(
          "<div class=\"chat-item\">\n" +
          "    <div class=\"chat-item-user\">\n" +
          message.text +
          "   </div>\n" +
          "</div>"
        );
      } else {
        chatList.append(
          "<div class=\"chat-item\">\n" +
          "    <div class=\"chat-item-friend\">\n" +
          message.text +
          "    </div>\n" +
          "</div>"
        );
      }
    };
    ws.onerror = function (evnt) {
      console.log(evnt)
    };
    ws.onclose = function (evnt) {
    };

    $("#btn1").click(function () {
      var input = $("#text");
      ws.send(input.val());
      input.val("");
    });
    $("#btn2").bind("click", function () {
      var url = "${ctx}/sendMsg";
      var input = $("#text");
      var content = input.val();
      input.val("");
      $.ajax({
        data: "content=" + content + "&fromUserName=" + "<%=name%>",
        type: "get",
        dataType: 'text',
        async: false,
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        encoding: "UTF-8",
        url: url,
        success: function (data) {
          console.log(data.toString());
        },
        error: function (msg) {
          alert(msg);
        }
      });
    })
  });

</script>
<body>
<div class="chat-header">
</div>
<div class="chat-body">
    <div id="displayAreaFriend" class="display-area-friend">
        <div class="profile-friend">

        </div>
    </div>
    <div id="chatList" class="chat-list">
    </div>
    <div id="displayAreaUser" class="display-area-user">
        <div class="profile-user">

        </div>
    </div>
</div>
<div class="chat-footer">
    <div class="footer-left">
    </div>
    <div class="area-input">
        <input class="input-chat" type="text" id="text" autofocus="autofocus"/>
        <button id="btn1" value="发送给后台">发送给后台</button>
        <button id="btn2" value="发送给其他用户">发送给其他用户</button>
    </div>
    <div class="footer-right">
    </div>
</div>
</body>
</html>
