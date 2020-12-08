<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>请求参数绑定</title>
</head>
<body>
<a href="/param/testParam?username=hehe&password=123">请求参数绑定</a>
<%--把数据封装Account类中，类中存在list和map集合--%>
<form action="/param/saveAccount" method="post">
    姓名：<input type="text" name="username"/><br>
    密码：<input type="text" name="password"/><br>
    金额：<input type="text" name="money"/><br>
    用户姓名：<input type="text" name="user.uname"/><br>
    用户年龄：<input type="text" name="user.age"/><br>
    <%--list--%>
    用户姓名：<input type="text" name="list[0].uname"/><br>
    用户年龄：<input type="text" name="list[0].age"/><br>
    <%--map--%>
    用户姓名：<input type="text" name="map['one'].uname"/><br>
    用户年龄：<input type="text" name="map['one'].age"/><br>
    </form>

    <input type="submit" value="提交">
<form action="/param/saveUser" method="post">

    用户姓名：<input type="text" name="uname"/><br>
    用户年龄：<input type="text" name="age"/><br>
    用户年龄：<input type="text" name="date"/><br>

    <input type="submit" value="提交">
</form>
<br>
<a href="/param/testServlet">测试原生API</a>

</form>
</body>
</html>
