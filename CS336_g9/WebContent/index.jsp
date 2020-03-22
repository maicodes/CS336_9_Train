<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<jsp:include page="header.jsp"></jsp:include>



<% if (request.getParameter("creation") != null) { %>
	<div class = "success box">Your account has been successfully created. You may log in now!</div>
<% } %>

<%
	Customer user = Auth.getLoginUser();
	String userFirstName = user.getFirstName();
	String userLastName = user.getLastName();
	out.print("Hello ");
	
	if(userFirstName != null)
		out.print(userFirstName + " " + userLastName);
	else
		out.print("User");
%>
<br>

<% if (userFirstName == null) { %>
<a href="auth.jsp">Login/Register</a>
<% } else { %>
<a href="Auth?action=logout">Log Out</a>
<% } %>

<jsp:include page="footer.jsp"></jsp:include>