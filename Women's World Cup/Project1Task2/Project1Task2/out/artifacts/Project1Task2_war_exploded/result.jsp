<%--
  Created by IntelliJ IDEA.
  User: changruidi
  Date: 2022/9/21
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>State: <%= request.getAttribute("State")  %>
</h2>
<h2>Population: <%= request.getAttribute("Population")  %>
</h2>
<h2>Nickname: <%= request.getAttribute("Nickname")  %>
</h2>
<h2>Capital: <%= request.getAttribute("Capital")  %>
</h2>
<h2>Song: <%= request.getAttribute("Song")  %>
</h2>

<h2>Flower: <img src="<%= request.getAttribute("Flower")  %>"></h2>
Credit: https://statesymbolsusa.org/categories/flower
<h2>Flag: <img src="<%= request.getAttribute("Flag")  %>"></h2>
Credit: https://states101.com/flags
<br>
<br>

<button onclick="back()">continue</button>
</body>
</html>

<script>
    function back(){
        window.location.replace("http://localhost:8080/Project1Task2_war_exploded/index.jsp");
    }
</script>