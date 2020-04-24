<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>



<% if (request.getParameter("creation") != null) { %>
	<div class = "success box">Your account has been successfully created. You may log in now!</div>
<% } %>

<div>
	<img id="travelingImg"
    src="traveling.svg" 
    alt="traveling image"
    height="300"
    width="500" />
</div>

<jsp:include page="footer.jsp"></jsp:include>