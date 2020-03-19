<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<jsp:include page="header.jsp"></jsp:include>
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
<a href="auth.jsp">Login/Register</a>
<jsp:include page="footer.jsp"></jsp:include>