<%@ page import="io.moomin.domain.User" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取数据</title>
</head>
<body>
<%
    User user = new User();
    user.setName("张三");
    user.setAge(23);
    user.setBirthday(new Date());
    request.setAttribute("user",user);
    ArrayList<Object> list = new ArrayList<>();
    list.add("aaa");
    list.add("bbb");
    list.add(user);
    request.setAttribute("list",list);

    Map map = new HashMap();
    map.put("sname", "lisi");
    map.put("gender", "nan");
    map.put("user", user);
    request.setAttribute("map",map);

%>
<h3>el获取对象中的值</h3>
${requestScope.user}<br>
<%--
    *通过的是对象的属性来获取
    *setter或getter方法,去掉set或get,在将剩余部分,首字母变小写
    *setName --> Name --> name(属性)
--%>
${requestScope.user.name}<br>
${user.age}<br>
${user.birthday.getMonth()}<br>
${user.birStr}
<h3>获取list值</h3>
${list}<br>
${list[0]}<br>
${list[1]}<br>
${list[2].name}
<h3>获取Map值</h3>
${map.gender}<br>
${map["gender"]}<br>
${map.user.name}
${not empty list}

</body>
</html>
