<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.swing.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>jstlb标签</title>
</head>
<body>
<c:if test="true">我是c...</c:if><br>
<%
    //判断request域中的list集合是否为空,如果不为null则显示遍历集合
    List list = new ArrayList();
    list.add("aaa");
    request.setAttribute("list",list);
    request.setAttribute("number",3);
%>
<c:if test="${not empty list}">
    遍历集合
</c:if>
<c:if test="${number % 2 != 0}">
    ${number}为奇数
</c:if>
<c:if test="${number % 2 == 0}">
    ${number}为偶数
</c:if>

<%--完成数字编号对应星期几
    域中存储数字
    使用choose取出数字: switch
    使用when做数字判断: case
    otherwise做其他情况的声明: default
--%>
<%
    request.setAttribute("num", 3);
    List list = new ArrayList();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    request.setAttribute("list", list);
%>
<c:choose>
    <c:when test="${num == 1}">星期一</c:when>
    <c:when test="${num == 2}">星期二</c:when>
    <c:when test="${num == 3}">星期三</c:when>
    <c:when test="${num == 4}">星期四</c:when>
    <c:when test="${num == 5}">星期五</c:when>
    <c:when test="${num == 6}">星期六</c:when>
    <c:when test="${num == 7}">星期七</c:when>
    <c:otherwise>数字输入有误</c:otherwise>
</c:choose>
<%--
    foreach:相当于java的for语句
    1.完成重复的操作
    for(int i = 0;i<10;i++){}
    属性:
        begin:开始值
        end:结束值
        var:临时变量
        step:步长
        varstatus:循环状态对象
            index:容器中元素的索引,从0开始
            count:循环次数,从1开始
    2.遍历容器
    List<User> list;
    for(User user : list){}
    属性:
        items:容器对象 list
        var:容器中运算的临时变量 user
        varstatus:循环状态对象
            index:容器中元素的索引,从0开始
            count:循环次数,从1开始
--%>
<c:forEach begin="1" end="10" var="i" step="1" varStatus="s" >
    ${i}<br>
</c:forEach>
<c:forEach items="${list}" var="str" varStatus="s">
    ${s.index} ${s.count} ${str}<br>
</c:forEach>
</body>
</html>
