<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery.min.js"></script>
    <script>
        $(function () {
            $("#btn").click(function () {
                $.ajax({
                    url: "/user/testAjax",
                    contentType: "application/json;charset=UTF-8",
                    data:'{"username":"hehe","password":"123","age","30"}',
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        alert(data)
                        alert(data.username);
                        alert(data.age);
                    }
                });
            });
        });
    </script>
</head>
<body>
<input id="btn" type="button" value="submit">
</body>
</html>
