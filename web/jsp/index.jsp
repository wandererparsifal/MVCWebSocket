<%--
  Created by IntelliJ IDEA.
  User: yangming
  Date: 18-10-10
  Time: 下午2:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="../js/jquery-3.3.1.min.js"></script>
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
      $("#msg").append(evnt.data + "<br>");
    };
    ws.onerror = function (evnt) {
      console.log(evnt)
    };
    ws.onclose = function (evnt) {
    };

    $("#btn1").click(function () {
      ws.send($("#text").val());
    });
    $("#btn2").bind("click", function () {
      var url = "${pageContext.request.contextPath}/websocket/sendMsg";
      var content = $("#text").val();
      var toUserName = "admin";
      $.ajax({
        data: "content=" + content,
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
当前登录用户：${pageContext.session.getAttribute("http-user").id}<br>
<input type="text" id="text">
<button id="btn1" value="发送给后台">发送给后台</button>
<button id="btn2" value="发送给其他用户">发送给其他用户</button>
<div id="msg"></div>
</body>
</html>
