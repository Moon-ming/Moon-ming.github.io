<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>查询所有用户</h3>
${list}<br>
<c:forEach items="${list}" var="account">
    ${account.name}
    ${account.money}<br>
</c:forEach>


</body>
</html>
