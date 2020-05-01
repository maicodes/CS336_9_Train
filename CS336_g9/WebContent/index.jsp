<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>

<%Object isCustomer = request.getSession(false).getAttribute("Customer");%>

<% if (request.getParameter("creation") != null) { %>
	<div class = "success box">Your account has been successfully created. You may log in now!</div>
<% } %>
<div style="color:blue">${ success }</div>
<%if (isCustomer != null) { %>
<!-- <div>Alerts:</div>  -->
<%} %>
<div>
	<img id="travelingImg"
    src="traveling.svg" 
    alt="traveling image"
    height="300"
    width="500" />
</div>



<jsp:include page="footer.jsp"></jsp:include>