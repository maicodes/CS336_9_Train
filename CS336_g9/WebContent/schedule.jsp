<%@ page import="pkg.Auth"%>
<%@ page import= "pkg.Models.Customer" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "pkg.Models.Train" %>
<%@ page import = "pkg.Models.Stop" %>
<%@ page import = "pkg.Common" %>
<%@ page import = "pkg.Models.StationTrain" %>
<%@ page import = "java.net.URLEncoder" %>
<jsp:include page = "header.jsp"></jsp:include>
<jsp:include page = "navigation.jsp"></jsp:include>
<% Common lib = new Common(); %>
<%-- <%
	if (!Auth.isLoggedIn())
	response.sendRedirect("index.jsp");
%> --%>

<h1>Train Schedule</h1>
<%

	if (!Auth.isLoggedIn(request)) {
		response.sendRedirect("auth.jsp");
	}
	if (request.getAttribute("transit_line_names") == null)
	{
			/* out.print("<p>An error has occured fetching the available Transit Lines. Please " + 
			"try again later or contact the site admin if this problem persists.");
 */	}
	else
		{
		@SuppressWarnings("unchecked")
		ArrayList<String> line_names = (ArrayList<String>) request.getAttribute("transit_line_names");
		
		for (String s : line_names)
		{
			out.print("<p><a href = Schedule?line=" + s.replaceAll("\\s","") + ">" + s + "</a></p>");
		}
		out.print("<a href = Schedule?line=all>Or view schedule by station.</a>");
	}
%>

<%
if (request.getAttribute("line_name") != null) {
	@SuppressWarnings("unchecked")
	ArrayList<Train> trains = (ArrayList<Train>) request.getAttribute("trains");
	Stop titlePointer = trains.get(0).get_trips().get(0);
	if (titlePointer.get_nextStop().get_position() < titlePointer.get_position())
	{
		titlePointer = trains.get(1).get_trips().get(0);
	}
	Stop stopPointer;
	String base = "05:00";
	String date = lib.getToday();
	if (request.getParameter("day") != null)
	{
		date = (String) request.getParameter("day");
	}
	ArrayList<String> dates = lib.getWeek();
%>
<h2>Displaying for <%=date %></h2>

<form method = "get">
	<label for = "day">View schedule for: </label>
	<select name = "day">
		<%
			for (String d : dates) {
		%>
			<option value = "<%=d %>"><%=d %></option>
		<%
			}
		%>
	</select>
	<input type = "hidden" name = "line" value = "<%=((String) request.getAttribute("line_name")).replaceAll("\\s","") %>">
	<input type = "submit">
</form>

<hr>

<%
int dep_arr = 0;
if (request.getParameter("dep_arr") != null)
{
	dep_arr = Integer.parseInt(request.getParameter("dep_arr"));
}
String dep_arr_info = "arrivals";
if (dep_arr != 0)
{
	dep_arr_info = "departures";
}
%>
<h3>Times listed are train <%=dep_arr_info %> </h3>
<form method = "get">
	<label>Select: </label>
	<input type = "radio" name = "dep_arr" value = "0">Arrival Time
	<input type = "radio" name = "dep_arr" value = "2">Departure Time
	<input type = "hidden" name = "day" value = "<%=date%>">
	<input type = "hidden" name = "line" value = "<%= ((String) request.getAttribute("line_name")).replaceAll("\\s","") %>">
	<input type = "submit" value = "Change">
</form>
<hr>
<h2>Forward Path</h2>
	<table border = 1 style = "white-space: nowrap;" class = "table table-striped table-responsive">
		<thead class = "thead-dark">
			<tr>
				<th>Train</th>
				<%
					while (titlePointer != null) {
				%>
				<th><%= titlePointer.get_station_name() %></th>
				<%
						titlePointer = titlePointer.get_nextStop();
					}
				%>
			</tr>
		</thead>
		<tbody>
			<%
				while (!base.equals("23:30")) {
			%>
			<tr>
				<%
					stopPointer = null;
					for (Train t : trains)
					{
						if (t.findTime(base) != null && (t.findTime(base).get_nextStop().get_position() > t.findTime(base).get_position()))
						{
							stopPointer = t.findTime(base);
				%>
						<td><%= t.getT_id() %>
				<%
						}
						
					}
					while (stopPointer != null) {
				%>
					<td><%= lib.addTime(stopPointer.get_arrive_time(), dep_arr) %></td>
				<%
				 		stopPointer = stopPointer.get_nextStop();
					}
				%>
			</tr>
			<%
					base = lib.addTime(base, 30);
				}
			%>
		</tbody>
	</table>
	<%
	base = "05:00";
	titlePointer = trains.get(1).get_trips().get(0);
	%>

<h2>Return Path</h2>
	<table border = 1 style = "white-space: nowrap;" class = "table table-striped table-responsive">
		<thead class = "thead-dark">
			<tr>
				<th>Train</th>
				<%
					while (titlePointer != null) {
				%>
				<th><%= titlePointer.get_station_name() %></th>
				<%
						titlePointer = titlePointer.get_nextStop();
					}
				%>
			</tr>
		</thead>
		<tbody>
			<%
				while (!base.equals("23:30")) {
			%>
			<tr>
				<%
					stopPointer = null;
					for (Train t : trains)
					{
						if (t.findTime(base) != null && (t.findTime(base).get_nextStop().get_position() < t.findTime(base).get_position()))
						{
							stopPointer = t.findTime(base);
				%>
						<td><%= t.getT_id() %>
				<%
						}
						
					}
					while (stopPointer != null) {
				%>
					<td><%= lib.addTime(stopPointer.get_arrive_time(), dep_arr) %></td>
				<%
				 		stopPointer = stopPointer.get_nextStop();
					}
				%>
			</tr>
			<%
					base = lib.addTime(base, 30);
				}
			%>
		</tbody>
	</table>
		
<% } %>


<%
	if (request.getAttribute("stations") != null)
	{
		@SuppressWarnings("unchecked")
		ArrayList<String> stations = (ArrayList<String>) request.getAttribute("stations");
%>
	<h2>Select <%= (String) request.getAttribute("selection") %> station:</h2>
<%
	String travelStart = (String) request.getAttribute("selection");
	travelStart = travelStart.toLowerCase();
		if (travelStart.equals("origin"))
		{
			for (String s : stations)
			{
				out.println("<p><a href = Schedule?" + travelStart + "=" + s.replaceAll("\\s","") + ">" + s + "</a></p>");
			}
		}
		else if (travelStart.equals("destination"))
		{
			String origin = request.getParameter("origin");
			for (String s : stations)
			{
				out.println("<p><a href = Schedule?origin=" + origin + "&destination=" + s.replaceAll("\\s","") + ">" + s + "</a></p>");
			}
		}
	}

%>
<%
if (request.getParameter("origin") != null && request.getParameter("destination") != null)
{

	String origin = (String) request.getAttribute("origin");
	String destination = (String) request.getAttribute("destination");
	
	String date = lib.getToday();
	if (request.getParameter("day") != null)
	{
		date = (String) request.getParameter("day");
	}
	ArrayList<String> dates = lib.getWeek();
%>


<h2><%=origin %> to <%= destination %></h2>
<hr>

<div class = "container">
<div class = "row">
<div class = "col">
<table class = "table table-striped" style = "width: 50%;" >
	<thead class = "thead-dark">
	<tr>
		<th>Train Number</th>
		<th>Origin Departure</th>
		<th>Destination Arrival</th>
	</tr>
	</thead>
	<tbody>
<%
	ArrayList<StationTrain> sch = StationTrain.list(origin, destination);
	for (StationTrain st : sch) {
%>
		<tr>
			<td><%=st.getId() %></td>
			<td><%=st.getDep() %></td>
			<td><%=st.getArr() %></td>
		</tr>
<%
	}
%>
</tbody>
</table>
</div>
<div class = "col">
	<h2>Displaying for <%=date %></h2>

<form method = "get">
	<label for = "day">View schedule for: </label>
	<select name = "day">
		<%
			for (String d : dates) {
		%>
			<option value = "<%=d %>"><%=d %></option>
		<%
			}
		%>
	</select>
	<input type = "hidden" name = "origin" value = "<%=((String) request.getAttribute("origin")).replaceAll("\\s","") %>">
	<input type = "hidden" name = "destination" value = "<%=((String) request.getAttribute("destination")).replaceAll("\\s","") %>">
	<input type = "submit">
</form>
<%
	String base_url = "Ticket";
%>
	<form action = "<%=base_url%>" method = "post">
		<input type = "hidden" name = "origin" value = "<%= (String) request.getParameter("origin") %>">
		<input type = "hidden" name = "destination" value = "<%= (String) request.getParameter("destination")%>">
		<button>Buy a ticket for this route</button>
	</form>
</div>
</div>
</div>



<%
}
%>

<jsp:include page="footer.jsp"></jsp:include>