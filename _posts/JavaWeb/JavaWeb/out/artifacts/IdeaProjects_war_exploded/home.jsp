<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="top.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:catch></c:catch>
<%-- --%>
<%
    pageContext.setAttribute("msg", "hello");//当前页面共享数据
    pageContext.getRequest();
%>
<%=pageContext.getAttribute("msg")%>
</body>
</html>
