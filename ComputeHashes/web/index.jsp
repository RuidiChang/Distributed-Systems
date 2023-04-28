<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>ComputeHashes</title>
</head>
<body>


<form action="submit" method="get">
  <br>
  <h2>Choose your encryption method: </h2>
  <br/><br/>
  <label><input name="encryptType" value="md5" required type="radio" checked="true"/>MD5</label>
  <label><input name="encryptType" value="sha256" required type="radio"/>SHA-256</label>
  <br>
  <br>
  Enter message <input type="text" name="textInput">
  <br>
  <br>
  <input type="submit" value="Submit"/>
</form>


</body>
</html>