<%--
  Created by IntelliJ IDEA.
  User: changruidi
  Date: 2022/9/16
  Time: 10:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h2>Hex: <%= request.getAttribute("hex")  %></h2>
<h2>Base64: <%= request.getAttribute("base64")  %></h2>

</body>
</html>
