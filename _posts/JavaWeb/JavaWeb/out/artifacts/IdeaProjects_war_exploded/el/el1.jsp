<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取域中的数据</title>
</head>
<body>
<%
    //在域中存储数据
    session.setAttribute("name","wangwu");

    request.setAttribute("name", "zhangsan");
    session.setAttribute("age", "23");
%>
<h3>获取值</h3>
${requestScope.name}
${sessionScope.age}
<h3>\${键名}</h3>
${name}
${sessionScope.name}
</body>
</html>
