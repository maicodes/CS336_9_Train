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
	<table border = 1 style = "white-space: nowrap;">
		<thead>
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
	titlePointer = trains.get(0).get_trips().get(0);
%>
<h2>Return Path</h2>
	<table border = 1 style = "white-space: nowrap;">
		<thead>
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
<hr>

<table border = 1>
	<tr>
		<th>Train Number</th>
		<th>Origin Departure</th>
		<th>Destination Arrival</th>
	</tr>
<%
	//My brain is a little slow right now so rather than do this in MySQL I'm just going to process the fetched data
	@SuppressWarnings("unchecked")
	ArrayList<Train> trains = (ArrayList<Train>) request.getAttribute("trains");
	ArrayList<StationTrain>entries = new ArrayList<StationTrain>();
	//First we should determine if it is a forward trip or a return trip
	//Let's make that function now and pass it along to the new servlet
	boolean forward_run = (Boolean) request.getAttribute("forward-run");
	//Okay now that we did that... we should do a nested loop through all the trains and where they stop and filter accordingly
	for (Train t : trains)
	{
		for (Stop s : t.get_trips())
		{
			//Hmmm.. how should we filter
			//If we're doing a forward run, we have to search for origin but make sure destination doesn't appear first
			//Why don't we just check if position of stop 2 > position stop 1?
			if (forward_run)
			{
			if (s.get_position() < s.get_nextStop().get_position())
			{
				StationTrain st = new StationTrain();
				while (s != null)
				{
					if (s.get_station_name().equals(origin)) {
						st.setId(t.getT_id());
						st.setDep(lib.addTime(s.get_arrive_time(), 2));
						st.setOrigin(s.get_stop_id());
					}
					if (s.get_station_name().equals(destination))
					{
						st.setArr(s.get_arrive_time());
						st.setDest(s.get_stop_id());
					}
					s = s.get_nextStop();
				}
				entries.add(st);
			}
			}
			else
			{
				if (s.get_position() > s.get_nextStop().get_position())
				{
					StationTrain st = new StationTrain();
					while (s != null)
					{
						if (s.get_station_name().equals(origin)) {
							st.setId(t.getT_id());
							st.setDep(lib.addTime(s.get_arrive_time(), 2));
							st.setOrigin(s.get_stop_id());
						}
						if (s.get_station_name().equals(destination))
						{
							st.setArr(s.get_arrive_time());
							st.setDest(s.get_stop_id());
						}
						s = s.get_nextStop();
					}
					entries.add(st);
				}
			}
		//Great, everythings out of order...
		//Maybe we should save everything into custom objects and re order everything through custom designed sort
		//Time spent thinking overhead > time spent doing everything lazy way
		
		//Now since everything's in the array list... gotta come up with a sort method
		}
	}
	
	entries = lib.sortTime(entries);
	
	for (StationTrain st : entries) {
%>
		<tr>
			<td><%=st.getId() %></td>
			<td><%=st.getDep() %></td>
			<td><%=st.getArr() %></td>
		</tr>
<%
	}
%>

</table>

<%
	String base_url = "index.jsp?";
%>

	<br>
	<form action = "<%=base_url%>">
		<input type = "hidden" name = "origin" value = "<%= (String) request.getParameter("origin") %>">
		<input type = "hidden" name = "destination" value = "<%= (String) request.getParameter("destination")%>">
		<button>Buy a ticket for this route</button>
	</form>

<%
}
%>

<jsp:include page="footer.jsp"></jsp:include>