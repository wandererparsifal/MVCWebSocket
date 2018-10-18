<%--
  Created by IntelliJ IDEA.
  User: yangming
  Date: 18-10-11
  Time: 下午8:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="../js/jquery-3.3.1.min.js"></script>
    <script>
      function login() {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username === "") {
          alert("请填写用户名");
        }
        if (password === "") {
          alert("请填写密码");
        }
        console.log("login " + username + " " + password);
        var url = "${ctx}/doLogin";
        console.log(url);
        $.ajax({
          url: url,
          type: "post",
          data: {
            username: username,
            password: password
          },
          success: function (data) {
            if (data.success) {
              window.location.href = "${ctx}/chat";
            } else if (data.failed) {
              alert(data.msg);
            }
          },
          error: function (data) {
            console.log("error " + data.toString());
          }
        });
      }
    </script>
</head>
<body>
<div style="text-align: center">
    <p></p>
    <input type="text" id="username">
    <p></p>
    <input type="text" id="password">
    <p></p>
    <button id="btn_login" value="登录" onclick="login()">登录</button>
</div>
</body>
</html>
