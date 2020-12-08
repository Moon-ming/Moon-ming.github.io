<%--
  Created by IntelliJ IDEA.
  User: 35194
  Date: 2020/10/16
  Time: 19:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" errorPage="500.jsp" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <h1>Hi ~ JSP</h1>


  <%
    System.out.println("hello jsp");
    String contextPath = request.getContextPath();
    out.print(contextPath);
  %>
  <%!
    int i = 3;
  %>
  <%=
    i
  %>
  <%
    response.getWriter().write("response...");
  %>
  </body>

</html>
