<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Contact us</title>
</head>
<body>
<div style="color:red">${ error }</div>
<h1>Contact us</h1>
	<form action="submitMessage" method="post">
	  <label for="username">Username:</label><br><br>
	  <input type="text" name="username" size="50"><br><br>
	  <label for="title">Title:</label><br><br>
	  <input type="text" name="title" size="50"><br><br>
	  <label for="mbox">Message:</label><br><br>
	  <input type="text" name="mbox" size="50"><br><br>
	  <button type="submit" class="auth-btn btn btn-primary">Submit</button>
	</form>
	
	

</body>
</html>