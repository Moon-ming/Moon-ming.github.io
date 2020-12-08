<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/anno/testRequstParam?username=haha">anno </a><br>
<form action="/anno/testRequstBody" method="post">

    用户姓名：<input type="text" name="uname"/><br>
    用户年龄：<input type="text" name="age"/><br>
    用户年龄：<input type="text" name="date"/><br>

    <input type="submit" value="提交">
</form><br>
<a href="/anno/testPathVariable/10">test PathVariable </a>

</body>
</html>
