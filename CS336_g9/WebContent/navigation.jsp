<%@ page import = "pkg.Auth, pkg.Common" %>
<%@ page import = "pkg.Models.Customer" %>
<%
	Customer user = new Customer();
	if (Auth.isLoggedIn(request))
	{
		user = Auth.getLoginUser();
	}
	
	Object isCustomer = request.getSession(false).getAttribute("Customer");
	Object isManager = request.getSession(false).getAttribute("Manager");
	Object isCR = request.getSession(false).getAttribute("CusRep");
	
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="index.jsp">Home</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
        <a class="nav-link" href="">
        Welcome
       <%
        	if (Auth.isLoggedIn(request))
        	{
        			out.println(user.getFirstName() + " " + user.getLastName());
        	}
        	else
        	{
        		out.println(" user");
        	}
        %>
        <span class="sr-only"></span></a>
      </li>
      <li class="nav-item">
      	<% if (!Auth.isLoggedIn(request)) {%>
        	<a class="nav-link" href="auth.jsp">Log In</a>
        <% } else { %>
        	<a class = "nav-link" href="Auth?action=logout">Log Out</a>
        <% } %>
      </li>
      <li class = "nav-item">
      	<a class = "nav-link" href = "Schedule">Schedules</a>
      </li>
      <% if (isCustomer != null) {%>
     		<li class = "nav-item">
        		<a class = "nav-link" href = "messages.jsp">Contact us</a>
        	</li>
        	<li class = "nav-item">
        		<a class = "nav-link" href = "browseCustomer.jsp">Browse messages</a>
      		</li>
      		<li class = "nav-item">
		      	<a class = "nav-link" href = "Schedule?line=all">Make a Reservation</a>
		    </li>
		    <li class = "nav-item">
		      	<a class = "nav-link" href = "reservations.jsp">View Reservations</a>
		    </li>
        <% } else if (isCR != null){ %>
        	<li class = "nav-item">
		      	<a class = "nav-link" href="browseCusRep.jsp">Messages</a>
		    </li>
        	<li class = "nav-item">
		      	<a class = "nav-link" href="cusRep.jsp">Customer Representative Page</a>
		    </li>
        <% } else if (isManager != null) { %>
        	<li class = "nav-item">
		      	<a class = "nav-link" href="admin.jsp">Manager Page</a>
		    </li>
        <%}%>
      
     
    <!-- 
    <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Dropdown
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="schedule.jsp">Schedules</a>
          <a class="dropdown-item" href="Schedule?line=all">Make a Reservation</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="messages.jsp">Messages</a>
        </div>
      </li>
     -->  
    </ul>
    <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>
</nav>


<jsp:include page = "footer.jsp"></jsp:include>
