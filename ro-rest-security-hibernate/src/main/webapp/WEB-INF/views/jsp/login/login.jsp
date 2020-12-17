<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<span class="badge badge-secondary">Login</span>
<c:if test = "${param.err == 1}">
<div id="errors" class="alert alert-danger" role="alert">
    Wrong credentials
</div>
</c:if>
<form action="/userLogin" method="POST">
  <div class="form-group">
    <label for="inputEmail">Username</label>
    <input type="text" class="form-control" id="inputEmail" name="username" placeholder="Enter Username">
  </div>
  <div class="form-group">
    <label for="inputPassword">Password</label>
    <input type="password" class="form-control" id="inputPassword" name="password" placeholder="Password">
  </div>
  <button type="submit" class="btn btn-primary">Submit</button>
</form>
</div>
</body>
</html>