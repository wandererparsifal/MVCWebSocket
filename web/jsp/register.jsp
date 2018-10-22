<%--
  Created by IntelliJ IDEA.
  User: yangming
  Date: 18-10-18
  Time: 下午2:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Register</title>
    <script src="../js/jquery-3.3.1.min.js"></script>
    <script>
      function register() {
        var username = $("#username").val();
        var password = $("#password").val();
        var confirm_password = $("#confirm_password").val();
        if (username === "") {
          alert("请填写用户名");
          return;
        }
        if (password === "") {
          alert("请填写密码");
          return;
        }
        if (password !== confirm_password) {
          alert("密码不一致");
          return;
        }
        var url = "${ctx}/doRegister";
        $.ajax({
          url: url,
          type: "post",
          data: {
            username: username,
            password: password
          },
          success: function (data) {
            if (0 === data.code) {
              window.location.href = "${ctx}/login";
            } else {
              alert(data.code);
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
    <input type="text" id="username" placeholder="username">
    <p></p>
    <input type="text" id="password" placeholder="password">
    <p></p>
    <input type="text" id="confirm_password" placeholder="confirm password">
    <p></p>
    <button id="btn_register" value="注册" onclick="register()">注册</button>
</div>
</body>
</html>
