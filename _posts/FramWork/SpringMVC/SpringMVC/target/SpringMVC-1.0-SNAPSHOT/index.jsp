<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/user/hello">hello springmvc</a>
<a href="/user/testRequestMapping?username=heihei" method="post">test RequestMapping</a>
<br/>
<form action="/user/testRequestMapping?username=heihei" method="post">
    <input type="submit" value="保存账户，post 请求">
</form>

<form action="/user/fileupload" method="post" enctype="multipart/form-data">
    选择文件：<input type="file" name="upload"/><br/>
    <input type="submit" value="上传文件"/>
</form>
</body>
</html>
