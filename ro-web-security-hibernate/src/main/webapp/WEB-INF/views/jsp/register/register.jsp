<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Register</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<span class="badge badge-secondary">Register</span>
<form:form method="POST" action="/register" modelAttribute="user" cssClass="form-horizontal">
  <div class="form-group">
    <label for="inputUsername">Username</label>
    <form:input path="userName" cssClass="form-control" id="inputUsername"/>
  </div>
  <div class="form-group">
    <label for="inputPassword">Password</label>
     <form:input path="password" cssClass="form-control" id="inputPassword"/>
  </div>
  <button type="submit" class="btn btn-primary">Register</button>
</form:form>
</div>
</body>
</html>