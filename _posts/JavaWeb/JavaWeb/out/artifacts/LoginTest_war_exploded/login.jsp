<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>login</title>
    <style>
        div{
            color: red;
        }
    </style>
</head>
<body>
<form action="/loginservlet1" method="post">
    <table>
        <tr>
            <td>用户名</td>
            <td><input type="text" name="username"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td>密码</td>
            <td><input type="password" name="password"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td>验证码</td>
            <td><input type="text" name="checkCode"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td colspan="2`"><img id="img" src="/CheckCodeServlet"></td>
            <a id="change" href="http://localhost/login.jsp">看不清换一张</a>
        </tr>
    </table>
    <table>
        <tr>
            <td colspan="2"><input type="submit" value="登录"></td>
        </tr>
    </table>
</form>
<div><%=request.getAttribute("cc_error") == null ? "" : request.getAttribute("cc_error") %></div>
<div><%=request.getAttribute("login_error") == null ? "" : request.getAttribute("login_error")%></div>
<script>
    window.onload = function () {
        document.getElementById("img").onclick = function () {
            this.src = "/CheckCodeServlet?time=" + new Date().getTime();
        };
    };
</script>
</body>
</html>